package com.resumebuilder.professionalexperience;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.resumetemplates.ResumeTemplatesServiceImplementation;
import com.resumebuilder.user.UserService;

import lombok.extern.slf4j.Slf4j;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.exception.ExperienceNotFoundException;
import com.resumebuilder.exception.ProfessionalExperienceException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

@Service
public class ProfessionalExperienceServiceImplementation implements ProfessionalExperienceService {
	
	@Autowired
	private ProfessionalExperienceRepository experienceRepo;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	 private static final Logger logger = LogManager.getLogger(ProfessionalExperienceServiceImplementation.class); 

	/**
     * Add a new experience.
     *
     * @param experience The experience to be added.
     * @return The added experience.
     * @throws ProfessionalExperienceException if the experience cannot be added.
     */	

	
	
	@Override
	public ProfessionalExperience addExperience(ProfessionalExperience experience,Principal principal) {
		
	User user = userRepository.findByEmail_Id(principal.getName());
		
		System.out.println(experience.getUser());
		
		if (experience == null) {
			throw new ProfessionalExperienceException("Professional Experience data is empty.");
		}
		try {
			ProfessionalExperience Proexperince = new ProfessionalExperience();
			Proexperince.setJob_title(experience.getJob_title());
			Proexperince.setOrganization_name(experience.getOrganization_name());
			Proexperince.setLocation(experience.getLocation());
			Proexperince.setStart_date(experience.getStart_date());
			Proexperince.setEnd_date(experience.getEnd_date());  
			Proexperince.setUser(user);
			
			return experienceRepo.save(Proexperince);
		} catch (Exception e) {
			throw new ProfessionalExperienceException("Error adding experince" + e);
		}
	}
	
	/**
     * Get a list of all experiences.
     *
     * @return A list of all experiences.
     * @throws ExperienceNotFoundException if no experiences are found.
     */
	
	@Override
	public List<ProfessionalExperience> getAllExperience() {
		
		 List<ProfessionalExperience> experiences = experienceRepo.findAll();

	        if (experiences.isEmpty()) {
	            throw new ExperienceNotFoundException("No experiences found.");
	        }
		return experiences;
	}

	/**
    * Get an experience by ID.
    *
    * @param id The ID of the experience to retrieve.
    * @return The experience with the specified ID if found, or an empty Optional.
    */
	
	@Override
	public Optional<ProfessionalExperience> getExperienceById(Long id) {
		// TODO Auto-generated method stub
		return experienceRepo.findById(id);
	}

	@Override
	public ProfessionalExperience updateExperienceById(Long id, ProfessionalExperience updatedExperience) throws ExperienceNotFoundException {
		
		ProfessionalExperience existingExperience = experienceRepo.findById(id)
                .orElseThrow(() -> new ExperienceNotFoundException("Experience not found"));

        // Copy the properties from the updatedExperience to the existingExperience
        existingExperience.setJob_title(updatedExperience.getJob_title());
        existingExperience.setOrganization_name(updatedExperience.getOrganization_name());
        existingExperience.setLocation(updatedExperience.getLocation());
        existingExperience.setStart_date(updatedExperience.getStart_date());
        existingExperience.setEnd_date(updatedExperience.getEnd_date());        
		
		return experienceRepo.save(existingExperience);
	}

	public String getTotalExperience(String userId) {
		String totalExperience="";
		// check weather the user Exists or not
		try {
			if(userService.checkUserExists(userId)){
				Integer exp= experienceRepo.getTotalExperience(userId);
				totalExperience=exp.toString();
			}else {
				logger.info("unable find the user-->"+userId);
			}
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			logger.info("unable find the user-->"+userId);
			totalExperience="";
		}catch(Exception e) {
			logger.info("error while counting experienc-->/n"+e);
		}
	   	return totalExperience;
	}

}

