/*
 * (C) Copyright 2021 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia.wdm.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.github.bonigarcia.wdm.SpringBootDemoApp;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Test using a local web application based on spring-boot.
 *
 * @author Boni Garcia (boni.gg@gmail.com)
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringBootDemoApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringChromeTest {

    WebDriver driver;

    @LocalServerPort
    int serverPort;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void test() {
        // Open system under test
        driver.get("http://localhost:" + serverPort);

        // Verify first page title
        String firstPageTitle = driver.getTitle();
        String expectedFirstPageTitle = "Spring Boot Test - Page 1";
        assertEquals(expectedFirstPageTitle, firstPageTitle);

        // Click on link
        driver.findElement(By.linkText("another")).click();

        // Verify second page caption
        String secondPageCaption = driver.findElement(By.id("caption"))
                .getText();
        String expectedSecondPageTitle = "Other page";
        assertEquals(expectedSecondPageTitle, secondPageCaption);
    }

}
