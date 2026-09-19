import React from 'react';
import './App.css';

function App() {
  // Define your microservices ecosystem here
  const services = [
    { 
      name: "Recipe Book UI", 
      description: "Full-stack React frontend interacting with the Spring Boot backend.", 
      url: "http://localhost:3000", 
      status: "Active",
      icon: "🍳" 
    },
    { 
      name: "API Gateway Entrypoint", 
      description: "Access the full visual application routed securely through the API Gateway.", 
      url: "http://localhost:8080", 
      status: "Running",
      icon: "🚪"
    },
    { 
      name: "Eureka Discovery", 
      description: "Netflix Eureka service registry dashboard (Port 8761).", 
      url: "http://localhost:8761", 
      status: "Planned",
      icon: "🧭" 
    },
    { 
      name: "Notification Service", 
      description: "Kafka event listener for background task processing.", 
      url: "#", 
      status: "Running",
      icon: "📬" 
    }
  ];

  return (
    <div className="portal-container">
      <header className="portal-header">
        <div className="header-content">
          <h1>Fridom Araya</h1>
          <p className="subtitle">Full Stack Java Developer | Microservices Architect</p>
          <div className="tech-stack">
            <span>Java 21</span> • <span>Spring Boot</span> • <span>React</span> • <span>Kafka</span> • <span>Kubernetes</span>
          </div>
        </div>
      </header>

      <main className="portal-main">
        <div className="section-title">
          <h2>Microservices Ecosystem</h2>
          <p>Select a service to launch its interface or dashboard.</p>
        </div>

        <div className="service-grid">
          {services.map((service, index) => (
            <a href={service.url} target="_blank" rel="noopener noreferrer" className="service-card" key={index}>
              <div className="card-icon">{service.icon}</div>
              <h3>{service.name}</h3>
              <p>{service.description}</p>
              <span className={`status-badge ${service.status.toLowerCase()}`}>
                {service.status}
              </span>
            </a>
          ))}
        </div>
      </main>
      
      <footer className="portal-footer">
        <p>&copy; {new Date().getFullYear()} Fridom Araya. All rights reserved.</p>
      </footer>
    </div>
  );
}

export default App;