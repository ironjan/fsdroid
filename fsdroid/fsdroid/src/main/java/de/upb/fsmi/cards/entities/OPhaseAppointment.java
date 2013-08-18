package de.upb.fsmi.cards.entities;

public class OPhaseAppointment {
	public final String appointment, location, start, end;

	public OPhaseAppointment(String start, String end, String appointment, String location) {
		super();
		this.start = start;
		this.end = end;
		this.appointment = appointment;
		this.location = location;
	}

}
