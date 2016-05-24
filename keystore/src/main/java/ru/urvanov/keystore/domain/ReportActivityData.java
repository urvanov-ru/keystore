package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
public class ReportActivityData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2799769596176848993L;


    @Id
    @Column(name="id_out")
    @JsonSerialize(using=ToStringSerializer.class)
    private Long id;
    
    @Column(name="date_begin_out")
    private Date dateBegin;
    
    @Column(name = "date_end_out")
    private Date dateEnd;
    
    @Column(name="clients_out")
    private Long clients;
    
    @Column(name = "active_clients_out")
    private Long activeClients;
    
    @Column(name = "not_active_clients_out")
    private Long notActiveClients;
    
    @Column(name = "connections_out")
    private BigInteger connections;
    
    @Column(name = "client_connections_out")
    private BigInteger clientConnections;
    
    @Column(name = "service_connections_out")
    private BigInteger serviceConnections;
    
    @Column(name = "sessions_time_out")
    private BigInteger sessionsTime;
    
    @Column(name = "client_sessions_time_out")
    private BigInteger clientSessionsTime;
    
    @Column(name = "service_sessions_time_out")
    private BigInteger serviceSessionsTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Long getClients() {
        return clients;
    }

    public void setClients(Long clients) {
        this.clients = clients;
    }

    public Long getActiveClients() {
        return activeClients;
    }

    public void setActiveClients(Long activeClients) {
        this.activeClients = activeClients;
    }

    public Long getNotActiveClients() {
        return notActiveClients;
    }

    public void setNotActiveClients(Long notActiveClients) {
        this.notActiveClients = notActiveClients;
    }

    public BigInteger getConnections() {
        return connections;
    }

    public void setConnections(BigInteger connections) {
        this.connections = connections;
    }

    public BigInteger getClientConnections() {
        return clientConnections;
    }

    public void setClientConnections(BigInteger clientConnections) {
        this.clientConnections = clientConnections;
    }

    public BigInteger getServiceConnections() {
        return serviceConnections;
    }

    public void setServiceConnections(BigInteger serviceConnections) {
        this.serviceConnections = serviceConnections;
    }

    public BigInteger getSessionsTime() {
        return sessionsTime;
    }

    public void setSessionsTime(BigInteger sessionsTime) {
        this.sessionsTime = sessionsTime;
    }

    public BigInteger getClientSessionsTime() {
        return clientSessionsTime;
    }

    public void setClientSessionsTime(BigInteger clientSessionsTime) {
        this.clientSessionsTime = clientSessionsTime;
    }

    public BigInteger getServiceSessionsTime() {
        return serviceSessionsTime;
    }

    public void setServiceSessionsTime(BigInteger serviceSessionsTime) {
        this.serviceSessionsTime = serviceSessionsTime;
    }

    @Override
    public String toString() {
        return "ReportActivityData [id=" + id + ", reportDate=" + dateBegin
                + ", clients=" + clients + ", activeClients=" + activeClients
                + ", notActiveClients=" + notActiveClients + ", connections="
                + connections + ", clientConnections=" + clientConnections
                + ", serviceConnections=" + serviceConnections
                + ", sessionsTime=" + sessionsTime + ", clientSessionsTime="
                + clientSessionsTime + ", serviceSessionsTime="
                + serviceSessionsTime + "]";
    }
    
    

}
