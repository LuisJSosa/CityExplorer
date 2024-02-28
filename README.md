# Microservice Project README

## Project Introduction

Welcome to our microservice project! This project is structured as a modern web application, consisting of two independently developed and deployed microservices:

- **Front-end**: Developed using Vue.js, this part of the application is responsible for presenting the user interface and interacting with the user. It makes requests to the back-end for data and updates the UI based on the responses received.
  
- **Back-end**: Developed using Spring Boot, this microservice acts as the server-side component that exposes RESTful API endpoints. It processes requests from the front-end, performs necessary logic or data fetching, and sends back the responses.

The primary function of this application is to provide weather-related information to users by allowing them to request weather data for specific cities. The back-end microservice fetches this data from an external weather API and then returns it to the front-end in a structured format.

## How to Programmatically REQUEST Data

To request data from our back-end microservice, you'll need to make an HTTP GET request to the appropriate API endpoint. The process is as follows:

1. **URL Construction**: Start with the base URL of the back-end microservice and append the endpoint path. For weather data, the endpoint is typically structured as `/api/{city}`, where `{city}` should be replaced with the name of the city for which you want weather data.

    Example URL:
    ```
    http://localhost:8080/api/{city}
    ```

2. **Making the Request**: Use an HTTP client to send a GET request to the constructed URL. You can use command-line tools like `curl`, libraries like `HttpClient` in Java, or `Axios` in JavaScript.

    Example using `curl`:
    ```
    curl http://localhost:8080/api/Raleigh
    ```

3. **Include Headers/Parameters**: If the API requires authentication or other parameters, include these in your request headers or query parameters.

    Example with API key in header:
    ```
    curl -H "X-Api-Key: YOUR_API_KEY" http://localhost:8080/api/Raleigh
    ```

4. **Receive the Response**: The response from the back-end will typically be in JSON format, containing the requested weather data.

## How to Programmatically RECEIVE Data

Upon making the request, your client will receive a response from the back-end microservice. Here's how to handle this response:

1. **Parsing the Response**: Extract the data from the response body. The format is usually JSON, so you'll need to parse this to a usable form in your programming language.

2. **Error Handling**: Implement error handling for scenarios such as network errors, API rate limits, or city not found. This usually involves checking the HTTP response status code and handling different cases accordingly.

3. **Using the Data**: Once parsed, use the data in your application as needed. For example, in a web application, you might update the DOM to display the weather information.

    Example using JavaScript and Axios:
    ```javascript
    axios.get('http://localhost:8080/api/Raleigh')
      .then(response => {
        console.log(response.data);  // Use this data to update your UI
      })
      .catch(error => {
        console.error('Error fetching data:', error);
      });
    ```

## UML Sequence Diagram

Below is a UML sequence diagram illustrating the interaction between the front-end and back-end microservices when requesting and receiving data:

```
[User] -> [Front-end]: Request weather for {city}
[Front-end] -> [Back-end]: GET /api/{city}
[Back-end] -> [External Weather API]: Fetch weather for {city}
[External Weather API] -> [Back-end]: Weather data
[Back-end] -> [Front-end]: Response with weather data
[Front-end] -> [User]: Display weather data
```
