package de.upb.fsmi.cards.entities;

import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.*;

@EBean
public class OPhaseKeeper {
	@StringArrayRes
	String[] ophaseMoStarts, ophaseMoEnds, ophaseMoAppointment, ophaseMoLocations, ophaseDiStarts,
			ophaseDiEnds, ophaseDiAppointments, ophaseDiLocations, ophaseMiStarts, ophaseMiEnds,
			ophaseMiAppointmens, ophaseMiLocations;
	private OPhaseAppointment[] dayOne = {};
	private OPhaseAppointment[] dayTwo = {};
	private OPhaseAppointment[] dayThree = {};

	@AfterInject
	void buildData() {
		buildDayOne();
		buildDayTwo();
		buildDayThree();
	}

	private void buildDayOne() {
		final int size = ophaseMoStarts.length;
		dayOne = new OPhaseAppointment[size];

		for (int i = 0; i < size; i++) {
			final String startString = ophaseMoStarts[i];
			final String endString = ophaseMoEnds[i];
			final String appointment = ophaseMoAppointment[i];
			final String location = ophaseMoLocations[i];
			dayOne[i] = new OPhaseAppointment(startString, endString, appointment, location);
		}
	}

	private void buildDayTwo() {
		final int size = ophaseDiStarts.length;
		dayTwo = new OPhaseAppointment[size];

		for (int i = 0; i < size; i++) {
			final String startString = ophaseDiStarts[i];
			final String endString = ophaseDiEnds[i];
			final String appointment = ophaseDiAppointments[i];
			final String location = ophaseDiLocations[i];
			dayTwo[i] = new OPhaseAppointment(startString, endString, appointment, location);
		}
	}

	private void buildDayThree() {
		final int size = ophaseMiStarts.length;
		dayThree = new OPhaseAppointment[size];

		for (int i = 0; i < size; i++) {
			final String startString = ophaseMiStarts[i];
			final String endString = ophaseMiEnds[i];
			final String appointment = ophaseMiAppointmens[i];
			final String location = ophaseMiLocations[i];
			dayThree[i] = new OPhaseAppointment(startString, endString, appointment, location);
		}
	}

	public OPhaseAppointment[] getDay(int day) {
		switch (day) {
		case 0:
			return dayOne;
		case 1:
			return dayTwo;
		case 2:
			return dayThree;
		default:
			final OPhaseAppointment[] empty = {};
			return empty;
		}
	}

}
