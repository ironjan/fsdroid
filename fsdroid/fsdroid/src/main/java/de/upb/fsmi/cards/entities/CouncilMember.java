package de.upb.fsmi.cards.entities;

public class CouncilMember {
	private final String nickName, realName, courseOfStudy, mail,
			canBeAskedAbout;
	private final CouncilJobs job;

	public CouncilMember(String nickName, String realName,
			String courseOfStudy, String mail, String canBeAskedAbout,
			CouncilJobs job) {
		super();
		this.nickName = nickName;
		this.realName = realName;
		this.courseOfStudy = courseOfStudy;
		this.mail = mail;
		this.canBeAskedAbout = canBeAskedAbout;
		this.job = job;
	}

	public String getNickName() {
		return nickName;
	}

	public String getRealName() {
		return realName;
	}

	public String getCourseOfStudy() {
		return courseOfStudy;
	}

	public String getMail() {
		return mail;
	}

	public String getCanBeAskedAbout() {
		return canBeAskedAbout;
	}

	public CouncilJobs getJob() {
		return job;
	}

}
