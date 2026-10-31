package com.fooddelivery.platform;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:food_delivery_test;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
class FoodDeliveryApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void restaurantAndMenuApisSupportTheirMainWorkflow() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));

        MvcResult restaurantResult = mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Green Bowl",
                                  "description": "Fresh local meals",
                                  "cuisineType": "Healthy",
                                  "address": "12 Market Road",
                                  "city": "Pune",
                                  "phoneNumber": "+91 9876543210"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Green Bowl"))
                .andReturn();

        Long restaurantId = jsonId(restaurantResult);

        mockMvc.perform(get("/api/restaurants")
                        .param("search", "green")
                        .param("city", "pune"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Green Bowl"));

        mockMvc.perform(get("/api/restaurants")
                        .param("cuisine", "healthy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].cuisineType").value("Healthy"));

        mockMvc.perform(get("/api/restaurants")
                        .param("cuisine", "Italian"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));

        MvcResult menuItemResult = mockMvc.perform(post("/api/restaurants/{id}/menu-items", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Avocado Bowl",
                                  "description": "Avocado and grains",
                                  "price": 12.50,
                                  "category": "Bowls",
                                  "vegetarian": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.restaurantId").value(restaurantId))
                .andReturn();

        Long menuItemId = jsonId(menuItemResult);

        mockMvc.perform(get("/api/restaurants/{id}/menu-items", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Avocado Bowl"));

        mockMvc.perform(put("/api/menu-items/{id}", menuItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Avocado Grain Bowl",
                                  "price": 13.00,
                                  "category": "Bowls",
                                  "vegetarian": true,
                                  "available": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));

        mockMvc.perform(get("/api/restaurants/{id}/menu-items", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        mockMvc.perform(get("/api/restaurants/{id}/menu-items", restaurantId)
                        .param("includeUnavailable", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Avocado Grain Bowl"));

        mockMvc.perform(delete("/api/restaurants/{id}", restaurantId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/restaurants/{id}", restaurantId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void invalidRestaurantAndMenuRequestsReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": " ",
                                  "cuisineType": "Healthy",
                                  "address": "12 Market Road",
                                  "city": "Pune"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        mockMvc.perform(post("/api/restaurants/1/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Invalid item",
                                  "price": 0,
                                  "category": "Meals"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    private Long jsonId(MvcResult result) throws Exception {
        String response = result.getResponse().getContentAsString();
        Object value = com.jayway.jsonpath.JsonPath.read(response, "$.id");
        assertNotNull(value);
        return ((Number) value).longValue();
    }
}
