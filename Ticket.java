package com.klef.fsad.exam;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Ticket Entity Class
 * Maps to the 'tickets' table in the 'fsadexam' MySQL database.
 *
 * Author  : 2400080194
 * Course  : Full Stack Application Development (FSAD)
 * Exam    : Skill-in-Sem Exam 1
 */
@Entity
@Table(name = "tickets")
public class Ticket {

    // ─────────────────────────────────────────────
    //  Fields / Columns
    // ─────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private int id;

    @Column(name = "ticket_name", nullable = false, length = 100)
    private String name;

    @Column(name = "ticket_date", nullable = false)
    private LocalDate date;

    @Column(name = "ticket_status", nullable = false, length = 30)
    private String status;           // e.g. OPEN, IN_PROGRESS, RESOLVED, CLOSED

    @Column(name = "ticket_category", length = 50)
    private String category;         // e.g. BUG, FEATURE, SUPPORT

    @Column(name = "ticket_priority", length = 20)
    private String priority;         // e.g. LOW, MEDIUM, HIGH, CRITICAL

    @Column(name = "assigned_to", length = 80)
    private String assignedTo;

    @Column(name = "description", length = 500)
    private String description;

    // ─────────────────────────────────────────────
    //  Constructors
    // ─────────────────────────────────────────────

    /** Default (no-arg) constructor required by Hibernate */
    public Ticket() {}

    /** Full constructor for easy object creation */
    public Ticket(String name, LocalDate date, String status,
                  String category, String priority,
                  String assignedTo, String description) {
        this.name        = name;
        this.date        = date;
        this.status      = status;
        this.category    = category;
        this.priority    = priority;
        this.assignedTo  = assignedTo;
        this.description = description;
    }

    // ─────────────────────────────────────────────
    //  Getters & Setters
    // ─────────────────────────────────────────────

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // ─────────────────────────────────────────────
    //  toString
    // ─────────────────────────────────────────────

    @Override
    public String toString() {
        return "Ticket {" +
                "id="          + id          +
                ", name='"     + name        + '\'' +
                ", date="      + date        +
                ", status='"   + status      + '\'' +
                ", category='" + category    + '\'' +
                ", priority='" + priority    + '\'' +
                ", assignedTo='"+ assignedTo + '\'' +
                ", description='"+ description +'\''+
                '}';
    }
}
