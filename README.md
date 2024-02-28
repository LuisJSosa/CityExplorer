# Microservice Project README

## Project Introduction

Welcome to our microservice project! This project is structured as a modern web application, consisting of two independently developed and deployed microservices:

- **Front-end**: Developed using Vue.js, this part of the application is responsible for presenting the user interface and interacting with the user. It makes requests to the back-end for data and updates the UI based on the responses received.
  
- **Back-end**: Developed using Spring Boot, this microservice acts as the server-side component that exposes RESTful API endpoints. It processes requests from the front-end, performs necessary logic or data fetching, and sends back the responses.

The primary function of this application is to provide weather-related information to users by allowing them to request weather data for specific cities. The back-end microservice fetches this data from an external weather API and then returns it to the front-end in a structured format.

## How to Programmatically REQUEST Data

To request data from our back-end microservice, you'll need to make an HTTP GET request to the appropriate API endpoint. Here's a step-by-step guide:

1. **URL Construction**: Begin with the base URL of the back-end microservice and append the specific endpoint path for fetching weather data. The standard format for these endpoints is `/api/{city}`, where `{city}` should be replaced with the actual city name you're interested in.

    Example URL construction:
    ```plaintext
    http://localhost:8080/api/Raleigh
    ```

2. **Making the Request**: Utilize an HTTP client to initiate a GET request to the constructed URL. This can be done through various methods depending on your development environment, such as using `curl` in the command line, `RestTemplate` in Spring Boot, or the `axios` library in JavaScript.

    Example using `curl`:
    ```plaintext
    curl http://localhost:8080/api/Raleigh
    ```

3. **Including Headers/Parameters**: Certain scenarios might require you to include additional headers or parameters in your request, such as API keys for authentication or additional query parameters for filtering data.

    Example with an API key header:
    ```plaintext
    curl -H "X-Api-Key: YOUR_API_KEY" http://localhost:8080/api/Raleigh
    ```

4. **Receiving the Response**: The microservice will respond with the requested data, typically in JSON format. This response contains the weather information for the specified city.

## How to Programmatically RECEIVE Data

After sending the request, the next step is to handle the incoming response from the microservice:

1. **Parsing the Response**: The response body, usually in JSON format, contains the weather data you requested. You'll need to parse this JSON data into a structure that your application can work with.

2. **Error Handling**: It's crucial to implement error handling for various failure scenarios, such as network issues, API limits being exceeded, or the requested city data not being available. This involves checking the response's HTTP status code and responding appropriately.

3. **Utilizing the Data**: Once the JSON data is parsed and any potential errors are handled, you can use the weather data in your application. This might involve updating the user interface with the received weather details, performing calculations, or storing the data for future use.

Below are examples of how to make a request to the microservice and handle the response using different technologies:

### Example using Spring Boot with Java

```java
import org.springframework.web.client.RestTemplate;

public class WeatherClient {
    public void fetchWeatherData(String city) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/" + city;
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("Received response: " + response);  // Process the JSON response
    }
}
```
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
 ```python
import requests

def fetch_weather_data(city):
    url = f"http://localhost:8080/api/{city}"
    response = requests.get(url)
    if response.status_code == 200:
        print(response.json())  # Process the JSON response as needed
    else:
        print("Error fetching data")

# Usage
fetch_weather_data("Raleigh")

 ```
 ```flask
from flask import Flask, jsonify
import requests

app = Flask(__name__)

@app.route('/fetch-weather/<city>')
def fetch_weather(city):
    url = f"http://localhost:8080/api/{city}"
    response = requests.get(url)
    if response.status_code == 200:
        return jsonify(response.json())  # Return the JSON response to the client
    else:
        return jsonify({"error": "Error fetching data"}), 500

if __name__ == '__main__':
    app.run(debug=True)
 ```

```Vue.js
import axios from 'axios';

export default {
  name: 'WeatherComponent',
  data() {
    return {
      weatherData: null,
    };
  },
  methods: {
    fetchWeatherData(city) {
      axios.get(`http://localhost:8080/api/${city}`)
        .then(response => {
          this.weatherData = response.data;  // Assign the data to a component data property
        })
        .catch(error => {
          console.error('Error fetching data:', error);
        });
    }
  },
  mounted() {
    this.fetchWeatherData('Raleigh');  // Example call when the component mounts
  }
}

```
## UML Sequence Diagram

Below is a UML sequence diagram illustrating the interaction between the front-end and back-end microservices when requesting and receiving data:

```
+-------+       +-----------+       +----------+       +-------------------+
| User  |       | Front-end |       | Back-end |       | External Weather  |
|       |       |           |       |          |       | API               |
+-------+       +-----------+       +----------+       +-------------------+
    |                 |                  |                    |
    | Request weather |                  |                    |
    | for {city}      |                  |                    |
    |---------------->|                  |                    |
    |                 | GET /api/{city}  |                    |
    |                 |-------------------------------------->|
    |                 |                  | Fetch weather for  |
    |                 |                  | {city}             |
    |                 |                  |<-------------------|
    |                 |                  | Weather data       |
    |                 |<--------------------------------------|
    |                 | Response with    |                    |
    |                 | weather data     |                    |
    |<----------------|                  |                    |
    | Display weather |                  |                    |
    | data            |                  |                    |
    +                 +                  +                    +

```
