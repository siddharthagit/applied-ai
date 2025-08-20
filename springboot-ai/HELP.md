
# Spring AI + Ollama Chat API

This project provides a simple **REST API** built with Spring Boot and [Spring AI](https://spring.io/projects/spring-ai) to interact with an [Ollama](https://ollama.ai) model.  
It supports both **blocking responses** and **streaming responses** (JSON & Server-Sent Events).

---

## 🚀 Features
- **Single-shot response** endpoint (`/ai/generate`)  
- **Reactive streaming (Flux) JSON responses** (`/ai/generateStream`)  
- **Streaming text responses for browsers (SSE)** (`/ai/generateStreamText`)  
- Clean logging with SLF4J  

---

## 🛠️ Requirements
- Java 17+  
- Maven 3.9+  
- [Ollama](https://ollama.ai) installed and running locally  
- A model pulled with Ollama (e.g. `llama2`, `mistral`, `gemma`, etc.)  

```bash
ollama run llama2
```

---

## ⚙️ Running the Application

Clone the repo and build:

```bash
mvn clean spring-boot:run
```

The app will start on **http://localhost:8080** by default.

---

## 📡 API Endpoints

### 1. Blocking Response
**GET** `/ai/generate?message=Hello`  

Returns a **plain text** response from the model.  

Example:
```bash
curl "http://localhost:8080/ai/generate?message=Tell me a joke"
```

Response:
```
Why don’t scientists trust atoms? Because they make up everything!
```

---

### 2. Streaming JSON Responses
**GET** `/ai/generateStream?message=Hello`  

Returns a **Flux stream of ChatResponse objects** (structured JSON).  
Useful for structured integrations or reactive clients.  

Example:
```bash
curl "http://localhost:8080/ai/generateStream?message=Tell me a story"
```

---

### 3. Streaming Text (SSE for Browsers)
**GET** `/ai/generateStreamText?message=Hello`  

Streams **plain text chunks** via **Server-Sent Events (SSE)**.  
Ideal for browser clients (like React, Vue, or vanilla JS `EventSource`).  

Example (curl with streaming):
```bash
curl -N "http://localhost:8080/ai/generateStreamText?message=Explain quantum physics"
```

Response (streamed chunks):
```
data: Quantum physics is the study...
data: of very small particles...
data: and how they interact...
```

---

## 🌐 Example Browser Client

You can test SSE streaming with this simple HTML:

```html
<!DOCTYPE html>
<html>
<body>
  <h3>Streaming Response</h3>
  <div id="output"></div>

  <script>
    const evtSource = new EventSource("http://localhost:8080/ai/generateStreamText?message=Tell+me+a+joke");
    evtSource.onmessage = (event) => {
      document.getElementById("output").innerHTML += event.data + " ";
    };
  </script>
</body>
</html>
```

Open in your browser and watch the AI respond in real-time.

---

## 📖 Notes
- Default port: `8080`  
- Default message: `"Tell me a joke"`  
- Logging: configurable in `application.properties`  

---

## 🧩 Next Steps
- Add authentication (e.g., API keys, JWT)  
- Containerize with Docker  
- Connect to multiple Ollama models  
