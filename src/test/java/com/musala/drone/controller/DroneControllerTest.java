package com.musala.drone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.musala.drone.dto.DroneDto;
import com.musala.drone.dto.MedicationDto;
import com.musala.drone.enums.DroneModel;
import com.musala.drone.enums.DroneState;
import com.musala.drone.service.DroneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DroneController.class)
class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DroneService droneService;

    @Test
    void registerDrone() throws Exception {
        DroneDto dto = new DroneDto(
                1L,
                "zephyr-15",
                DroneModel.LIGHTWEIGHT,
                100,
                99,
                DroneState.IDLE,
                new ArrayList<>());
        doNothing().when(droneService).registerDrone(dto);

        this.mockMvc
                .perform(post("/drone/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(dto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void loadDrone() throws Exception {
        ArrayList<MedicationDto> medicationList = new ArrayList<>(1);
        medicationList.add(new MedicationDto(
                1L,
                "ibuprofen",
                5,
                "IBU_5",
                "http://example.com"
        ));
        DroneDto dto = new DroneDto(
                1L,
                "zephyr-15",
                DroneModel.LIGHTWEIGHT,
                100,
                99,
                DroneState.IDLE,
                new ArrayList<>(1));
        doNothing().when(droneService).loadDrone(medicationList, dto.getSerialNumber());

        this.mockMvc
                .perform(post("/drone/" + dto.getSerialNumber() + "/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(medicationList)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getMedications() throws Exception {

        ArrayList<MedicationDto> medicationList = new ArrayList<>(1);
        medicationList.add(new MedicationDto(
                1L,
                "ibuprofen",
                5,
                "IBU_5",
                "http://example.com"
        ));

        DroneDto dto = new DroneDto(
                1L,
                "zephyr-15",
                DroneModel.LIGHTWEIGHT,
                100,
                99,
                DroneState.IDLE,
                medicationList);
        when(droneService.getMedications(dto.getSerialNumber())).thenReturn(dto.getMedicationList());

        this.mockMvc
                .perform(get("/drone/" + dto.getSerialNumber() + "/medications"))
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .json(mapToJson(dto.getMedicationList()))
                );
    }

    @Test
    void getBatteryLevel() throws Exception {
        DroneDto dto = new DroneDto(
                1L,
                "zephyr-15",
                DroneModel.LIGHTWEIGHT,
                100,
                99,
                DroneState.IDLE,
                new ArrayList<>(1));
        when(droneService.getBatteryLevel(dto.getSerialNumber())).thenReturn(dto.getBatteryCapacity());

        this.mockMvc
                .perform(get("/drone/" + dto.getSerialNumber() + "/battery"))
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .json(mapToJson(dto.getBatteryCapacity()))
                );
    }

    @Test
    void getAllAvailableForLoadingDrones() throws Exception {
        DroneDto dto = new DroneDto(
                1L,
                "zephyr-15",
                DroneModel.LIGHTWEIGHT,
                100,
                99,
                DroneState.IDLE,
                new ArrayList<>(1));
        when(droneService.getAllAvailableForLoadingDrones()).thenReturn(List.of(dto));
        this.mockMvc
                .perform(get("/drone/all-available-for-loading-drones"))
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .json(mapToJson(List.of(dto)))
                );
    }

    private static String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }
}