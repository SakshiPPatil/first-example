package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "<!DOCTYPE html>\n" +
               "<html>\n" +
               "<head>\n" +
               "    <title>Spring Boot Demo Application</title>\n" +
               "    <style>\n" +
               "        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }\n" +
               "        .container { max-width: 800px; margin: 0 auto; background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n" +
               "        h1 { color: #2c3e50; text-align: center; }\n" +
               "        .endpoint { background-color: #f8f9fa; padding: 15px; margin: 10px 0; border-left: 4px solid #007bff; }\n" +
               "        .endpoint-method { font-weight: bold; color: #007bff; }\n" +
               "        .endpoint-path { font-family: monospace; }\n" +
               "        .endpoint-desc { color: #6c757d; }\n" +
               "        .note { background-color: #fff3cd; padding: 15px; margin: 20px 0; border-left: 4px solid #ffc107; }\n" +
               "    </style>\n" +
               "</head>\n" +
               "<body>\n" +
               "    <div class=\"container\">\n" +
               "        <h1>🚀 Spring Boot Demo Application</h1>\n" +
               "        <p>Welcome to the Spring Boot Demo Application! This is a simple REST API for user management.</p>\n" +
               "        \n" +
               "        <div class=\"note\">\n" +
               "            <strong>Note:</strong> This application stores data in memory only. All data will be lost when the application restarts.\n" +
               "        </div>\n" +
               "        \n" +
               "        <h2>Available Endpoints:</h2>\n" +
               "        \n" +
               "        <div class=\"endpoint\">\n" +
               "            <span class=\"endpoint-method\">GET</span>\n" +
               "            <span class=\"endpoint-path\">/</span>\n" +
               "            <p class=\"endpoint-desc\">This welcome page</p>\n" +
               "        </div>\n" +
               "        \n" +
               "        <div class=\"endpoint\">\n" +
               "            <span class=\"endpoint-method\">GET</span>\n" +
               "            <span class=\"endpoint-path\">/api/users</span>\n" +
               "            <p class=\"endpoint-desc\">Retrieve all users</p>\n" +
               "        </div>\n" +
               "        \n" +
               "        <div class=\"endpoint\">\n" +
               "            <span class=\"endpoint-method\">POST</span>\n" +
               "            <span class=\"endpoint-path\">/api/users</span>\n" +
               "            <p class=\"endpoint-desc\">Create a new user (requires JSON body with name and email)</p>\n" +
               "        </div>\n" +
               "        \n" +
               "        <div class=\"endpoint\">\n" +
               "            <span class=\"endpoint-method\">GET</span>\n" +
               "            <span class=\"endpoint-path\">/api/users/{id}</span>\n" +
               "            <p class=\"endpoint-desc\">Retrieve a specific user by ID</p>\n" +
               "        </div>\n" +
               "        \n" +
               "        <div class=\"endpoint\">\n" +
               "            <span class=\"endpoint-method\">GET</span>\n" +
               "            <span class=\"endpoint-path\">/api/info</span>\n" +
               "            <p class=\"endpoint-desc\">Get application information</p>\n" +
               "        </div>\n" +
               "        \n" +
               "        <div class=\"endpoint\">\n" +
               "            <span class=\"endpoint-method\">GET</span>\n" +
               "            <span class=\"endpoint-path\">/actuator/health</span>\n" +
               "            <p class=\"endpoint-desc\">Check application health status</p>\n" +
               "        </div>\n" +
               "        \n" +
               "        <h2>Example Usage:</h2>\n" +
               "        <p>Create a user with curl:</p>\n" +
               "        <pre>curl -X POST http://localhost:8080/api/users \\\n" +
               "  -H \"Content-Type: application/json\" \\\n" +
               "  -d '{\"name\":\"John Doe\",\"email\":\"john@example.com\"}'</pre>\n" +
               "        \n" +
               "        <p>Get all users:</p>\n" +
               "        <pre>curl http://localhost:8080/api/users</pre>\n" +
               "        \n" +
               "        <p>Check application health:</p>\n" +
               "        <pre>curl http://localhost:8080/actuator/health</pre>\n" +
               "    </div>\n" +
               "</body>\n" +
               "</html>";
    }
}