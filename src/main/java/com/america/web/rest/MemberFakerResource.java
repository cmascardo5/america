package com.america.web.rest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.america.domain.Member;
import com.america.service.MemberService;
import com.github.javafaker.Faker;
// import resources.ZipCode.csv;

@RestController
@RequestMapping("/api/member-faker")
public class MemberFakerResource {
    
    private final Logger log = LoggerFactory.getLogger(MemberFakerResource.class);
    private final MemberService memberService;
    private final Faker faker = new Faker(new Locale("en-US"));
    // private final Map<String, ZipCode> zipCodes = new HashMap<String, ZipCode>();
    private final Random rand = new Random();
    private int counter;

    @Autowired
    private ResourceLoader resourceLoader;

    public MemberFakerResource(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/create")
    public String create(@RequestParam(defaultValue = "1000") int count) {
        List<Member> members = new ArrayList<Member>();
        for (counter = 0; counter< count; counter++) {
            Member member = new Member();
            generateMemberDemographics(member);
            
        }

        return "create";
    }

    private Long age;

    private void generateMemberDemographics(Member member) {
        member.setFirstName(faker.name().firstName());
        member.setLastName(faker.name().lastName());
        member.setAddress(faker.address().buildingNumber() + " " + faker.address().streetName());
        member.setCity(faker.address().city());
        member.setZip(faker.address().zipCode());
        // member.setCounty(fixCountyByZipCode);
        member.setBirthDate(convertToLocalDate(faker.date().birthday()));
        age = LocalDate.from(member.getBirthDate()).until(LocalDate.now(), ChronoUnit.YEARS);

        member.setGender(faker.demographic().sex());
        // member.setrace(faker.demographic().race());
        // member.setmarital(faker.demographic().maritalStatus())

        if (rand.nextBoolean()) {
            member.setTobaccoUseIndicator(true);
        } else {
            member.setTobaccoUseIndicator(false);
        }

        if (rand.nextBoolean()) {
            member.setSubstanceAbuseIndicator(true);
        } else {
            member.setSubstanceAbuseIndicator(false);
        }

    }
    


    private LocalDate convertToLocalDate(java.util.Date date) {
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
