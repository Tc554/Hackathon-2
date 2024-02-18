package me.tastycake.hackathon_2.chatgpt;

import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Setter
public class GPTAPI {
    private String apiKey;
    private static String url = "https://api.openai.com/v1/chat/completions";
    private Model model;
    HttpURLConnection connection;
    private double t = 0.5;

    public GPTAPI(String apiKey, Model model) {
        this.apiKey = apiKey;
        this.model = model;
    }

    public String sendFunction(GPTPrompt[] prompts, GPTFunction[] functions) {
        try {
            try {
                URL obj = new URL(url);
                connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + apiKey);
                connection.setRequestProperty("Content-Type", "application/json");

// ...

                JSONObject body = new JSONObject();
                body.put("model", model.name);

                JSONArray messagesArray = new JSONArray();
                for (var prompt : prompts) {
                    JSONObject message = new JSONObject();
                    message.put("role", prompt.type.name);
                    message.put("content", prompt.data);
                    messagesArray.put(message);
                }
                body.put("messages", messagesArray);

                // Functions
                JSONArray functionsArray = new JSONArray();
                for (var functionData : functions) {
                    JSONObject function = new JSONObject();
                    function.put("name", functionData.getName());
                    function.put("description", functionData.getDescription());
                    JSONObject parameters = new JSONObject();
                    parameters.put("type", "object");
                    JSONObject properties = new JSONObject();
                    properties.put(functionData.getParm(), new JSONObject()
                            .put("type", "string")
                            .put("description", functionData.getParmsDescription()));
                    properties.put(functionData.getParm2(), new JSONObject()
                            .put("type", "string")
                            .put("description", functionData.getParmsDescription()));
                    //properties.put("unit", new JSONObject()
                    //                        .put("type", "string")
                    //                        .put("enum", new JSONArray().put("celsius").put("fahrenheit")));
                    parameters.put("properties", properties);
                    parameters.put("required", new JSONArray().put(functionData.getParm()).put(functionData.getParm2()));
                    function.put("parameters", parameters);
                    functionsArray.put(function);
                }


                body.put("functions", functionsArray);

                body.put("function_call", "auto");
                body.put("temperature", t);

                String requestBody = body.toString();


                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(body.toString());
                writer.flush();
                writer.close();

// Response from ChatGPT
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                StringBuffer response = new StringBuffer();

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

// Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());

// Check if there's a function call
                if (jsonResponse.has("choices")) {
                    JSONArray choices = jsonResponse.getJSONArray("choices");
                    if (choices.length() > 0 && choices.getJSONObject(0).has("message")) {
                        JSONObject message = choices.getJSONObject(0).getJSONObject("message");
                        if (message.has("function_call")) {
                            JSONObject functionCall = message.getJSONObject("function_call");
                            // Handle the function call. For example, if the function is "get_current_weather":
                            for (var function : functions) {
                                if (function.getName().equals(functionCall.getString("name"))) {
                                    JSONObject functionArgs = new JSONObject(functionCall.getString("arguments"));
                                    String args = functionArgs.getString(function.getParm());

                                    String[] returnData;

                                    if (functionArgs.getString(function.getParm2()) == null) {
                                        returnData = function.getAction().run(args);
                                    } else {
                                        returnData = function.getAction().run(args, functionArgs.getString(function.getParm2()));
                                    }

                                    JSONObject functionResponseMessage = new JSONObject();
                                    functionResponseMessage.put("role", "function");
                                    functionResponseMessage.put("name", function.getName());
                                    functionResponseMessage.put("content", returnData);
                                    messagesArray.put(functionResponseMessage);

                                    JSONObject newResponse = sendRequestToGPT(body);
                                    return extractMessageFromJSONResponse(newResponse.toString());
                                }
                            }
                        }
                    }
                }

                return extractMessageFromJSONResponse(response.toString());

            } catch (IOException e) {
//                // Read the error stream to get the detailed error message
//                InputStream errorStream = connection.getErrorStream();
//                BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
//                StringBuilder errorResponse = new StringBuilder();
//                String errorLine;
//                while ((errorLine = errorReader.readLine()) != null) {
//                    errorResponse.append(errorLine);
//                }
//                errorReader.close();
//
//                // Print or log the detailed error message
//                JSONObject errorJson = new JSONObject(errorResponse.toString());
//                if (errorJson.has("error")) {
//                    JSONObject errorDetails = errorJson.getJSONObject("error");
//                    String errorMessage = errorDetails.getString("message");
//                    String errorType = errorDetails.getString("type");
//                    // String errorCode = errorDetails.getString("code");
//
//                    System.out.println("Error Type: " + errorType);
//                    // System.out.println("Error Code: " + errorCode);
//                    System.out.println("Error Message: " + errorMessage);
//                }
//                throw new BizException(Error.SERVER_HAD_AN_ERROR.getCode(), e.getMessage());

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }


        return null;
    }

    private JSONObject sendRequestToGPT(JSONObject body) throws Exception {
        URL obj = new URL(url);
        connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(body.toString());
        writer.flush();
        writer.close();

        // Response from ChatGPT
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuffer response = new StringBuffer();

        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();

        return new JSONObject(response.toString());
    }


    public String send(GPTPrompt[] prompts) {
        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // The request body with the desired messages
            StringBuilder body = new StringBuilder("{\"model\": \"" + model.name + "\", \"messages\": [");

            //+ "{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"},"
            //                    + "{\"role\": \"user\", \"content\": \"" + prompt + "\"}"
            int i = 0;
            for (var prompt : prompts) {
                body.append("{\"role\": \""+prompt.type.name+"\", \"content\": \""+prompt.data+"\"}"); //TODO , fixer
                i++;
                if (i < prompts.length) body.append(",");
            }

            body.append("]}");

            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body.toString());
            writer.flush();
            writer.close();

            // Response from ChatGPT
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuffer response = new StringBuffer();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // calls the method to extract the message.
            return extractMessageFromJSONResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content")+ 11;

        int end = response.indexOf("\"", start);

        return response.substring(start, end);

    }

//    public static Map<String, Object> extractFunctionCallFromJSONResponse(String response) {
//        Map<String, Object> result = new HashMap<>();
//        ObjectMapper mapper = new ObjectMapper();
//
//        try {
//            // Assuming the function call is in a field named "function_call" in the response
//            FunctionCall functionCall = mapper.readValue(response, FunctionCall.class);
//
//            if (functionCall.getName().equals("get_current_weather")) {
//                result.put("functionName", functionCall.getName());
//                result.put("location", functionCall.getArguments().get("location"));
//                result.put("unit", functionCall.getArguments().get("unit"));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }


    public static enum Model {
        gpt_3_5_turbo("gpt-3.5-turbo"),
        gpt_3_5_turbo_0613("gpt-3.5-turbo-0613");

        String name;

        Model(String name) {
            this.name = name;
        }
    }
}
