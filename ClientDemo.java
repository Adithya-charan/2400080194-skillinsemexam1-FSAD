package com.klef.fsad.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * ClientDemo — Demonstrates Hibernate HQL Operations on the Ticket entity.
 *
 *  I.  Insert records using a persistent object (session.save).
 *  II. Update Name and Status by ID using HQL with positional parameters (?1, ?2 …).
 *
 * Author  : 2400080194
 * Course  : Full Stack Application Development (FSAD)
 * Exam    : Skill-in-Sem Exam 1
 * DB      : fsadexam
 * Package : com.klef.fsad.exam
 */
public class ClientDemo {

    // ──────────────────────────────────────────────────────────
    //  Shared SessionFactory (one per application)
    // ──────────────────────────────────────────────────────────
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure("hibernate.cfg.xml")   // loads src/main/resources/hibernate.cfg.xml
                    .buildSessionFactory();
        } catch (Exception ex) {
            System.err.println("SessionFactory creation failed: " + ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    // ──────────────────────────────────────────────────────────
    //  MAIN
    // ──────────────────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("=======================================================");
        System.out.println("  FSAD Exam — Hibernate HQL Demo on Ticket Entity      ");
        System.out.println("  Student ID : 2400080194                               ");
        System.out.println("=======================================================\n");

        // ── I. Insert Records ──────────────────────────────────
        System.out.println("──────────────────────────────────────────────");
        System.out.println(" PART I : Inserting Ticket Records             ");
        System.out.println("──────────────────────────────────────────────");

        insertTicket(new Ticket(
                "Login Page Crash",
                LocalDate.of(2024, 1, 10),
                "OPEN",
                "BUG",
                "HIGH",
                "Alice Johnson",
                "Application crashes on login when password exceeds 20 characters."
        ));

        insertTicket(new Ticket(
                "Add Dark Mode Feature",
                LocalDate.of(2024, 1, 15),
                "IN_PROGRESS",
                "FEATURE",
                "MEDIUM",
                "Bob Smith",
                "Users have requested a dark mode toggle in settings."
        ));

        insertTicket(new Ticket(
                "Payment Gateway Timeout",
                LocalDate.of(2024, 1, 18),
                "OPEN",
                "BUG",
                "CRITICAL",
                "Carol Davis",
                "Payment API times out after 30 seconds under load."
        ));

        insertTicket(new Ticket(
                "Export Report to PDF",
                LocalDate.of(2024, 1, 20),
                "RESOLVED",
                "FEATURE",
                "LOW",
                "David Lee",
                "Allow users to export monthly reports as PDF files."
        ));

        insertTicket(new Ticket(
                "Database Connection Pool",
                LocalDate.of(2024, 1, 22),
                "OPEN",
                "SUPPORT",
                "HIGH",
                "Eve Martin",
                "Connection pool exhaustion observed during peak hours."
        ));

        // ── Display all records after insert ───────────────────
        System.out.println("\n--- All Tickets After Insert ---");
        displayAllTickets();

        // ── II. Update Name & Status by ID using HQL ──────────
        System.out.println("\n──────────────────────────────────────────────");
        System.out.println(" PART II : Updating Name & Status via HQL      ");
        System.out.println("          (Positional Parameters)               ");
        System.out.println("──────────────────────────────────────────────");

        // Update Ticket with ID=1: change name and status
        updateTicketNameAndStatus(1, "Login Page Crash - FIXED", "RESOLVED");

        // Update Ticket with ID=2: change name and status
        updateTicketNameAndStatus(2, "Dark Mode Feature - Completed", "CLOSED");

        // Update Ticket with ID=3: change status only (name update too)
        updateTicketNameAndStatus(3, "Payment Gateway Timeout - Under Review", "IN_PROGRESS");

        // ── Display all records after update ───────────────────
        System.out.println("\n--- All Tickets After Update ---");
        displayAllTickets();

        // ── Retrieve a single ticket by ID ─────────────────────
        System.out.println("\n--- Retrieve Ticket by ID (HQL) ---");
        getTicketById(1);
        getTicketById(3);

        // ── Retrieve tickets filtered by Status ───────────────
        System.out.println("\n--- Retrieve Tickets by Status (HQL) ---");
        getTicketsByStatus("RESOLVED");

        // ── Close factory ──────────────────────────────────────
        sessionFactory.close();
        System.out.println("\n=======================================================");
        System.out.println("  Demo Completed Successfully!                          ");
        System.out.println("=======================================================");
    }

    // ──────────────────────────────────────────────────────────
    //  I.  INSERT — Using Persistent Object (session.save)
    // ──────────────────────────────────────────────────────────
    /**
     * Persists a Ticket object to the database.
     * Uses session.save() to insert the record (persistent object approach).
     *
     * @param ticket the Ticket object to insert
     */
    public static void insertTicket(Ticket ticket) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            // Persistent Object: session.save() makes the object persistent
            // Hibernate will generate and execute the INSERT SQL automatically
            int generatedId = (int) session.save(ticket);

            transaction.commit();

            System.out.println("✔ Inserted  →  " + ticket.getName() +
                               "  [Generated ID = " + generatedId + "]");

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("✘ Insert Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ──────────────────────────────────────────────────────────
    //  II. UPDATE — Name & Status by ID using HQL Positional Params
    // ──────────────────────────────────────────────────────────
    /**
     * Updates the Name and Status of a Ticket identified by its ID.
     *
     * HQL used:
     *   UPDATE Ticket t SET t.name = ?1, t.status = ?2 WHERE t.id = ?3
     *
     * Positional parameters: ?1 = newName, ?2 = newStatus, ?3 = ticketId
     *
     * @param ticketId  ID of the ticket to update
     * @param newName   new name/title for the ticket
     * @param newStatus new status value (e.g. RESOLVED, CLOSED)
     */
    public static void updateTicketNameAndStatus(int ticketId, String newName, String newStatus) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            // ── HQL Update with Positional Parameters ──
            String hql = "UPDATE Ticket t SET t.name = ?1, t.status = ?2 WHERE t.id = ?3";

            Query<?> query = session.createQuery(hql);
            query.setParameter(1, newName);     // ?1 → newName
            query.setParameter(2, newStatus);   // ?2 → newStatus
            query.setParameter(3, ticketId);    // ?3 → ticketId

            int rowsAffected = query.executeUpdate();

            transaction.commit();

            if (rowsAffected > 0) {
                System.out.println("✔ Updated ID=" + ticketId +
                                   "  →  Name='" + newName +
                                   "'  Status='" + newStatus + "'  (" + rowsAffected + " row)");
            } else {
                System.out.println("⚠ No record found with ID=" + ticketId);
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("✘ Update Failed for ID=" + ticketId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ──────────────────────────────────────────────────────────
    //  HELPER — Display All Tickets (HQL SELECT)
    // ──────────────────────────────────────────────────────────
    /**
     * Retrieves and prints all Ticket records using HQL SELECT.
     */
    public static void displayAllTickets() {
        try (Session session = sessionFactory.openSession()) {

            String hql = "FROM Ticket t ORDER BY t.id ASC";
            Query<Ticket> query = session.createQuery(hql, Ticket.class);
            List<Ticket> tickets = query.getResultList();

            if (tickets.isEmpty()) {
                System.out.println("  [No records found]");
            } else {
                System.out.printf("  %-4s %-40s %-12s %-12s %-10s %-15s%n",
                        "ID", "Name", "Date", "Status", "Priority", "Assigned To");
                System.out.println("  " + "-".repeat(100));
                for (Ticket t : tickets) {
                    System.out.printf("  %-4d %-40s %-12s %-12s %-10s %-15s%n",
                            t.getId(),
                            truncate(t.getName(), 38),
                            t.getDate(),
                            t.getStatus(),
                            t.getPriority(),
                            t.getAssignedTo());
                }
            }

        } catch (Exception e) {
            System.err.println("✘ Display Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ──────────────────────────────────────────────────────────
    //  HELPER — Get Ticket by ID (HQL with positional param)
    // ──────────────────────────────────────────────────────────
    /**
     * Retrieves a single Ticket by its ID using HQL positional parameter.
     *
     * @param ticketId ID to look up
     */
    public static void getTicketById(int ticketId) {
        try (Session session = sessionFactory.openSession()) {

            String hql = "FROM Ticket t WHERE t.id = ?1";
            Query<Ticket> query = session.createQuery(hql, Ticket.class);
            query.setParameter(1, ticketId);

            Ticket ticket = query.uniqueResult();

            if (ticket != null) {
                System.out.println("  Found → " + ticket);
            } else {
                System.out.println("  No ticket found with ID=" + ticketId);
            }

        } catch (Exception e) {
            System.err.println("✘ GetById Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ──────────────────────────────────────────────────────────
    //  HELPER — Get Tickets by Status (HQL with positional param)
    // ──────────────────────────────────────────────────────────
    /**
     * Retrieves all Tickets matching a given status using HQL positional parameter.
     *
     * @param status the status to filter by
     */
    public static void getTicketsByStatus(String status) {
        try (Session session = sessionFactory.openSession()) {

            String hql = "FROM Ticket t WHERE t.status = ?1 ORDER BY t.id";
            Query<Ticket> query = session.createQuery(hql, Ticket.class);
            query.setParameter(1, status);

            List<Ticket> tickets = query.getResultList();

            System.out.println("  Tickets with status='" + status + "': " + tickets.size() + " found");
            for (Ticket t : tickets) {
                System.out.println("    → [ID=" + t.getId() + "] " + t.getName());
            }

        } catch (Exception e) {
            System.err.println("✘ GetByStatus Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ──────────────────────────────────────────────────────────
    //  Utility
    // ──────────────────────────────────────────────────────────
    private static String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 1) + "…";
    }
}
