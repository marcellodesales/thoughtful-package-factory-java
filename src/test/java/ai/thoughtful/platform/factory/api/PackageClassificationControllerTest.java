package ai.thoughtful.platform.factory.api;

import ai.thoughtful.platform.factory.AutomationFactoryService;
import ai.thoughtful.platform.factory.algorithm.PackageClassificationType;
import ai.thoughtful.platform.factory.algorithm.StackType;
import ai.thoughtful.platform.factory.model.Package;
import ai.thoughtful.platform.factory.model.PackageFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.EnumSet;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for PackageSortingController REST API endpoints.
 */
@WebMvcTest(PackageClassificationController.class)
class PackageClassificationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AutomationFactoryService service;

    @Test
    void testClassifyStandardPackage() throws Exception {
        Package testPackage = PackageFactory.builder()
                .withHeightInCm(30)
                .withLengthInCm(20)
                .withWidthInCm(50)
                .withMassInGrams(5000).build();

        // Mock all the API class internally
        when(service.makeNewPackage(testPackage.dimension().width(), testPackage.dimension().height(),
                testPackage.dimension().length(), testPackage.mass()))
                .thenReturn(testPackage);
        when(service.classifyPackage(testPackage)).thenReturn(EnumSet.noneOf(PackageClassificationType.class));

        // Perform the HTTP request to the API, verifying the body of the Json returned
        mockMvc.perform(get("/api/v1/packages/classify")
                        .param("width", testPackage.dimension().width() + "")
                        .param("height", testPackage.dimension().height() + "")
                        .param("length", testPackage.dimension().length() + "")
                        .param("mass", testPackage.mass() + ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.packageInfo.dimension.height").value(testPackage.dimension().height()))
                .andExpect(jsonPath("$.packageInfo.dimension.width").value(testPackage.dimension().width()))
                .andExpect(jsonPath("$.packageInfo.dimension.length").value(testPackage.dimension().length()))
                .andExpect(jsonPath("$.packageInfo.dimension.volume").value(testPackage.dimension().getVolume()))
                .andExpect(jsonPath("$.packageInfo.mass").value(testPackage.mass()))
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.timestamp").exists());
    }

}

