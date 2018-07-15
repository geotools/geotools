package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Attributes {

    /** (Required) */
    @SerializedName("LGA")
    @Expose
    private Object lGA;
    /** (Required) */
    @SerializedName("MetropolitanRural")
    @Expose
    private Object metropolitanRural;
    /** (Required) */
    @SerializedName("Departmental_Region")
    @Expose
    private Object departmentalRegion;
    /** (Required) */
    @SerializedName("Departmental_Area")
    @Expose
    private Object departmentalArea;
    /** (Required) */
    @SerializedName("Area_of_LGA_sq_km")
    @Expose
    private Object areaOfLGASqKm;
    /** (Required) */
    @SerializedName("ASGS_LGA_code")
    @Expose
    private Object aSGSLGACode;
    /** (Required) */
    @SerializedName("Most_populous_town_or_suburb_in")
    @Expose
    private Object mostPopulousTownOrSuburbIn;
    /** (Required) */
    @SerializedName("Distance_to_Melbourne_in_km")
    @Expose
    private Object distanceToMelbourneInKm;
    /** (Required) */
    @SerializedName("Travel_time_from_Melbourne_GPO_")
    @Expose
    private Object travelTimeFromMelbourneGPO;
    /** (Required) */
    @SerializedName("ARIA_Remoteness_category")
    @Expose
    private Object aRIARemotenessCategory;
    /** (Required) */
    @SerializedName("Percent_Business_land_use")
    @Expose
    private Object percentBusinessLandUse;
    /** (Required) */
    @SerializedName("Percent_Industrial_land_use")
    @Expose
    private Object percentIndustrialLandUse;
    /** (Required) */
    @SerializedName("Percent_Residential_land_use")
    @Expose
    private Object percentResidentialLandUse;
    /** (Required) */
    @SerializedName("Percent_Rural_land_use")
    @Expose
    private Object percentRuralLandUse;
    /** (Required) */
    @SerializedName("Percent_Other_land_use")
    @Expose
    private Object percentOtherLandUse;
    /** (Required) */
    @SerializedName("Per_annum_pop_change_actual_for")
    @Expose
    private Object perAnnumPopChangeActualFor;
    /** (Required) */
    @SerializedName("Per_annum_pop_change_projected_")
    @Expose
    private Object perAnnumPopChangeProjected;
    /** (Required) */
    @SerializedName("Females0to14yrs")
    @Expose
    private Object females0to14yrs;
    /** (Required) */
    @SerializedName("Females15to24yrs")
    @Expose
    private Object females15to24yrs;
    /** (Required) */
    @SerializedName("Females25to44yrs")
    @Expose
    private Object females25to44yrs;
    /** (Required) */
    @SerializedName("Females45to64yrs")
    @Expose
    private Object females45to64yrs;
    /** (Required) */
    @SerializedName("Females65to84yrs")
    @Expose
    private Object females65to84yrs;
    /** (Required) */
    @SerializedName("Female85yrsPlus")
    @Expose
    private Object female85yrsPlus;
    /** (Required) */
    @SerializedName("TotalFemales")
    @Expose
    private Object totalFemales;
    /** (Required) */
    @SerializedName("Males0to14yrs")
    @Expose
    private Object males0to14yrs;
    /** (Required) */
    @SerializedName("Males15to24yrs")
    @Expose
    private Object males15to24yrs;
    /** (Required) */
    @SerializedName("Males25to44yrs")
    @Expose
    private Object males25to44yrs;
    /** (Required) */
    @SerializedName("Males45to64yrs")
    @Expose
    private Object males45to64yrs;
    /** (Required) */
    @SerializedName("Males65to84yrs")
    @Expose
    private Object males65to84yrs;
    /** (Required) */
    @SerializedName("Male85yrsPlus")
    @Expose
    private Object male85yrsPlus;
    /** (Required) */
    @SerializedName("TotalMales")
    @Expose
    private Object totalMales;
    /** (Required) */
    @SerializedName("Total0to14yrs")
    @Expose
    private Object total0to14yrs;
    /** (Required) */
    @SerializedName("Tota15to24yrs")
    @Expose
    private Object tota15to24yrs;
    /** (Required) */
    @SerializedName("Tota25to44yrs")
    @Expose
    private Object tota25to44yrs;
    /** (Required) */
    @SerializedName("Tota45to64yrs")
    @Expose
    private Object tota45to64yrs;
    /** (Required) */
    @SerializedName("Tota65to84yrs")
    @Expose
    private Object tota65to84yrs;
    /** (Required) */
    @SerializedName("Tota85yrsPlus")
    @Expose
    private Object tota85yrsPlus;
    /** (Required) */
    @SerializedName("Total2013ERP")
    @Expose
    private Object total2013ERP;
    /** (Required) */
    @SerializedName("Percent_Total0to14yrs")
    @Expose
    private Object percentTotal0to14yrs;
    /** (Required) */
    @SerializedName("PercentTotal15to24yrs")
    @Expose
    private Object percentTotal15to24yrs;
    /** (Required) */
    @SerializedName("PercentTotal25to44yrs")
    @Expose
    private Object percentTotal25to44yrs;
    /** (Required) */
    @SerializedName("PercentTotal45to64yrs")
    @Expose
    private Object percentTotal45to64yrs;
    /** (Required) */
    @SerializedName("PercentTotal65to84yrs")
    @Expose
    private Object percentTotal65to84yrs;
    /** (Required) */
    @SerializedName("PercentTotal84yrsPlus")
    @Expose
    private Object percentTotal84yrsPlus;
    /** (Required) */
    @SerializedName("Total_fertility_rate_2012")
    @Expose
    private Object totalFertilityRate2012;
    /** (Required) */
    @SerializedName("Rank_Total_fertility_rate_2012")
    @Expose
    private Object rankTotalFertilityRate2012;
    /** (Required) */
    @SerializedName("Teenage_fertility_rate_2012")
    @Expose
    private Object teenageFertilityRate2012;
    /** (Required) */
    @SerializedName("Rank_Teenage_fertility_rate_201")
    @Expose
    private Object rankTeenageFertilityRate201;
    /** (Required) */
    @SerializedName("Percent_Aboriginal_or_Torres_St")
    @Expose
    private Object percentAboriginalOrTorresSt;
    /** (Required) */
    @SerializedName("Rank_Aboriginal_or_Torres_Strai")
    @Expose
    private Object rankAboriginalOrTorresStrai;
    /** (Required) */
    @SerializedName("Percent_Born_overseas_2011")
    @Expose
    private Object percentBornOverseas2011;
    /** (Required) */
    @SerializedName("Rank_Percent_Born_overseas_2011")
    @Expose
    private Object rankPercentBornOverseas2011;
    /** (Required) */
    @SerializedName("Born_in_a_non_English_speaking_")
    @Expose
    private Object bornInANonEnglishSpeaking;
    /** (Required) */
    @SerializedName("Rank_Born_in_a_non_English_spea")
    @Expose
    private Object rankBornInANonEnglishSpea;
    /** (Required) */
    @SerializedName("Country_1_Percent_for_Top_5_ove")
    @Expose
    private Object country1PercentForTop5Ove;
    /** (Required) */
    @SerializedName("Country_1_for_Top_5_overseas_co")
    @Expose
    private Object country1ForTop5OverseasCo;
    /** (Required) */
    @SerializedName("Country_2_Percent_for_Top_5_ove")
    @Expose
    private Object country2PercentForTop5Ove;
    /** (Required) */
    @SerializedName("Country_2_for_Top_5_overseas_co")
    @Expose
    private Object country2ForTop5OverseasCo;
    /** (Required) */
    @SerializedName("Country_3_Percent_for_Top_5_ove")
    @Expose
    private Object country3PercentForTop5Ove;
    /** (Required) */
    @SerializedName("Country_3_for_Top_5_overseas_co")
    @Expose
    private Object country3ForTop5OverseasCo;
    /** (Required) */
    @SerializedName("Country_4_Percent_for_Top_5_ove")
    @Expose
    private Object country4PercentForTop5Ove;
    /** (Required) */
    @SerializedName("Country_4_for_Top_5_overseas_co")
    @Expose
    private Object country4ForTop5OverseasCo;
    /** (Required) */
    @SerializedName("Country_5_Percent_for_Top_5_ove")
    @Expose
    private Object country5PercentForTop5Ove;
    /** (Required) */
    @SerializedName("Country_5_for_Top_5_overseas_co")
    @Expose
    private Object country5ForTop5OverseasCo;
    /** (Required) */
    @SerializedName("Percent_Speaks_LOTE_at_home")
    @Expose
    private Object percentSpeaksLOTEAtHome;
    /** (Required) */
    @SerializedName("Rank_Percent_Speaks_LOTE_at_hom")
    @Expose
    private Object rankPercentSpeaksLOTEAtHom;
    /** (Required) */
    @SerializedName("Country_1_Percent_Top_5_languag")
    @Expose
    private Object country1PercentTop5Languag;
    /** (Required) */
    @SerializedName("Country_1_Top_5_languages_spoke")
    @Expose
    private Object country1Top5LanguagesSpoke;
    /** (Required) */
    @SerializedName("Country_2_Percent_Top_5_languag")
    @Expose
    private Object country2PercentTop5Languag;
    /** (Required) */
    @SerializedName("Country_2_Top_5_languages_spoke")
    @Expose
    private Object country2Top5LanguagesSpoke;
    /** (Required) */
    @SerializedName("Country_3_Percent_Top_5_languag")
    @Expose
    private Object country3PercentTop5Languag;
    /** (Required) */
    @SerializedName("Country_3_Top_5_languages_spoke")
    @Expose
    private Object country3Top5LanguagesSpoke;
    /** (Required) */
    @SerializedName("Country_4_Percent_Top_5_languag")
    @Expose
    private Object country4PercentTop5Languag;
    /** (Required) */
    @SerializedName("Country_4_Top_5_languages_spoke")
    @Expose
    private Object country4Top5LanguagesSpoke;
    /** (Required) */
    @SerializedName("Country_5_Percent_Top_5_languag")
    @Expose
    private Object country5PercentTop5Languag;
    /** (Required) */
    @SerializedName("Country_5_Top_5_languages_spoke")
    @Expose
    private Object country5Top5LanguagesSpoke;
    /** (Required) */
    @SerializedName("Percent_Low_English_proficiency")
    @Expose
    private Object percentLowEnglishProficiency;
    /** (Required) */
    @SerializedName("Rank_Percent_Low_English_profic")
    @Expose
    private Object rankPercentLowEnglishProfic;
    /** (Required) */
    @SerializedName("Ancestry_1_Percent_Top_5_ancest")
    @Expose
    private Object ancestry1PercentTop5Ancest;
    /** (Required) */
    @SerializedName("Ancestry_1_Top_5_ancestries")
    @Expose
    private Object ancestry1Top5Ancestries;
    /** (Required) */
    @SerializedName("Ancestry_2_Percent_Top_5_ancest")
    @Expose
    private Object ancestry2PercentTop5Ancest;
    /** (Required) */
    @SerializedName("Ancestry_2_Top_5_ancestries")
    @Expose
    private Object ancestry2Top5Ancestries;
    /** (Required) */
    @SerializedName("Ancestry_3_Percent_Top_5_ancest")
    @Expose
    private Object ancestry3PercentTop5Ancest;
    /** (Required) */
    @SerializedName("Ancestry_3_Top_5_ancestries")
    @Expose
    private Object ancestry3Top5Ancestries;
    /** (Required) */
    @SerializedName("Ancestry_4_Percent_Top_5_ancest")
    @Expose
    private Object ancestry4PercentTop5Ancest;
    /** (Required) */
    @SerializedName("Ancestry_4_Top_5_ancestries")
    @Expose
    private Object ancestry4Top5Ancestries;
    /** (Required) */
    @SerializedName("Ancestry_5_Percent_Top_5_ancest")
    @Expose
    private Object ancestry5PercentTop5Ancest;
    /** (Required) */
    @SerializedName("Ancestry_5_Top_5_ancestries")
    @Expose
    private Object ancestry5Top5Ancestries;
    /** (Required) */
    @SerializedName("New_settler_arrivals_per_100000")
    @Expose
    private Object newSettlerArrivalsPer100000;
    /** (Required) */
    @SerializedName("Rank_New_settler_arrivals_per_1")
    @Expose
    private Object rankNewSettlerArrivalsPer1;
    /** (Required) */
    @SerializedName("Humanitarian_arrivals_as_a_Perc")
    @Expose
    private Object humanitarianArrivalsAsAPerc;
    /** (Required) */
    @SerializedName("Rank_Humanitarian_arrivals_as_a")
    @Expose
    private Object rankHumanitarianArrivalsAsA;
    /** (Required) */
    @SerializedName("Community_acceptance_of_diverse")
    @Expose
    private Object communityAcceptanceOfDiverse;
    /** (Required) */
    @SerializedName("Rank_Community_acceptance_of_di")
    @Expose
    private Object rankCommunityAcceptanceOfDi;
    /** (Required) */
    @SerializedName("Proportion_of_households_with_b")
    @Expose
    private Object proportionOfHouseholdsWithB;
    /** (Required) */
    @SerializedName("Households_with_broadband_inter")
    @Expose
    private Object householdsWithBroadbandInter;
    /** (Required) */
    @SerializedName("Gaming_machine_losses_per_head_")
    @Expose
    private Object gamingMachineLossesPerHead;
    /** (Required) */
    @SerializedName("Rank_Gaming_machine_losses_per_")
    @Expose
    private Object rankGamingMachineLossesPer;
    /** (Required) */
    @SerializedName("Family_Incidents_per_1000_pop")
    @Expose
    private Object familyIncidentsPer1000Pop;
    /** (Required) */
    @SerializedName("Rank_Family_Incidents_per_1000_")
    @Expose
    private Object rankFamilyIncidentsPer1000;
    /** (Required) */
    @SerializedName("Drug_usage_and_possession_offen")
    @Expose
    private Object drugUsageAndPossessionOffen;
    /** (Required) */
    @SerializedName("Rank_Drug_usage_and_possession_")
    @Expose
    private Object rankDrugUsageAndPossession;
    /** (Required) */
    @SerializedName("Total_Crime_per_1000_pop")
    @Expose
    private Object totalCrimePer1000Pop;
    /** (Required) */
    @SerializedName("Rank_Total_Crime_per_1000_pop")
    @Expose
    private Object rankTotalCrimePer1000Pop;
    /** (Required) */
    @SerializedName("Feels_safe_walking_alone_during")
    @Expose
    private Object feelsSafeWalkingAloneDuring;
    /** (Required) */
    @SerializedName("Rank_Feels_safe_walking_alone_d")
    @Expose
    private Object rankFeelsSafeWalkingAloneD;
    /** (Required) */
    @SerializedName("Believe_other_people_can_be_tru")
    @Expose
    private Object believeOtherPeopleCanBeTru;
    /** (Required) */
    @SerializedName("Rank_Believe_other_people_can_b")
    @Expose
    private Object rankBelieveOtherPeopleCanB;
    /** (Required) */
    @SerializedName("Spoke_with_more_than_5_people_t")
    @Expose
    private Object spokeWithMoreThan5PeopleT;
    /** (Required) */
    @SerializedName("Rank_Spoke_with_more_than_5_peo")
    @Expose
    private Object rankSpokeWithMoreThan5Peo;
    /** (Required) */
    @SerializedName("Able_to_definitely_get_help_fro")
    @Expose
    private Object ableToDefinitelyGetHelpFro;
    /** (Required) */
    @SerializedName("Rank_Able_to_definitely_get_hel")
    @Expose
    private Object rankAbleToDefinitelyGetHel;
    /** (Required) */
    @SerializedName("Volunteers")
    @Expose
    private Object volunteers;
    /** (Required) */
    @SerializedName("Rank_Volunteers")
    @Expose
    private Object rankVolunteers;
    /** (Required) */
    @SerializedName("Feel_valued_by_society")
    @Expose
    private Object feelValuedBySociety;
    /** (Required) */
    @SerializedName("Rank_Feel_valued_by_society")
    @Expose
    private Object rankFeelValuedBySociety;
    /** (Required) */
    @SerializedName("Attended_a_local_community_even")
    @Expose
    private Object attendedALocalCommunityEven;
    /** (Required) */
    @SerializedName("Rank_Attended_a_local_community")
    @Expose
    private Object rankAttendedALocalCommunity;
    /** (Required) */
    @SerializedName("Take_action_on_behalf_of_the_lo")
    @Expose
    private Object takeActionOnBehalfOfTheLo;
    /** (Required) */
    @SerializedName("Rank_Take_action_on_behalf_of_t")
    @Expose
    private Object rankTakeActionOnBehalfOfT;
    /** (Required) */
    @SerializedName("Members_of_a_sports_group")
    @Expose
    private Object membersOfASportsGroup;
    /** (Required) */
    @SerializedName("Rank_Members_of_a_sports_group")
    @Expose
    private Object rankMembersOfASportsGroup;
    /** (Required) */
    @SerializedName("Members_of_a_religious_group")
    @Expose
    private Object membersOfAReligiousGroup;
    /** (Required) */
    @SerializedName("Rank_Members_of_a_religious_gro")
    @Expose
    private Object rankMembersOfAReligiousGro;
    /** (Required) */
    @SerializedName("Rated_their_community_as_an_act")
    @Expose
    private Object ratedTheirCommunityAsAnAct;
    /** (Required) */
    @SerializedName("Rank_Rated_their_community_as_a")
    @Expose
    private Object rankRatedTheirCommunityAsA;
    /** (Required) */
    @SerializedName("Rated_their_community_as_a_plea")
    @Expose
    private Object ratedTheirCommunityAsAPlea;
    /** (Required) */
    @SerializedName("Rank_ated_their_community_as_a_")
    @Expose
    private Object rankAtedTheirCommunityAsA;
    /** (Required) */
    @SerializedName("Rated_their_community_as_good_o")
    @Expose
    private Object ratedTheirCommunityAsGoodO;
    /** (Required) */
    @SerializedName("Rank_Rated_their_community_as_g")
    @Expose
    private Object rankRatedTheirCommunityAsG;
    /** (Required) */
    @SerializedName("Index_of_Relative_Socia_Economi")
    @Expose
    private Object indexOfRelativeSociaEconomi;
    /** (Required) */
    @SerializedName("Rank_Index_of_Relative_Socia_Ec")
    @Expose
    private Object rankIndexOfRelativeSociaEc;
    /** (Required) */
    @SerializedName("Unemployment_rate")
    @Expose
    private Object unemploymentRate;
    /** (Required) */
    @SerializedName("Rank_Unemployment_rate")
    @Expose
    private Object rankUnemploymentRate;
    /** (Required) */
    @SerializedName("Percent_Individual_income_Less_")
    @Expose
    private Object percentIndividualIncomeLess;
    /** (Required) */
    @SerializedName("Rank_Percent_Individual_income_")
    @Expose
    private Object rankPercentIndividualIncome;
    /** (Required) */
    @SerializedName("Percent_Female_income_Less_than")
    @Expose
    private Object percentFemaleIncomeLessThan;
    /** (Required) */
    @SerializedName("Rank_Percent_Female_income_Less")
    @Expose
    private Object rankPercentFemaleIncomeLess;
    /** (Required) */
    @SerializedName("Percent_Male_income_Less_than_4")
    @Expose
    private Object percentMaleIncomeLessThan4;
    /** (Required) */
    @SerializedName("Rank_Percent_Male_income_Less_t")
    @Expose
    private Object rankPercentMaleIncomeLessT;
    /** (Required) */
    @SerializedName("Percent_One_Parent_headed_famil")
    @Expose
    private Object percentOneParentHeadedFamil;
    /** (Required) */
    @SerializedName("Rank_Percent_One_Parent_headed_")
    @Expose
    private Object rankPercentOneParentHeaded;
    /** (Required) */
    @SerializedName("One_Parent_headed_families_Perc")
    @Expose
    private Object oneParentHeadedFamiliesPerc;
    /** (Required) */
    @SerializedName("Rank_One_Parent_headed_families")
    @Expose
    private Object rankOneParentHeadedFamilies;
    /** (Required) */
    @SerializedName("One_Parent_headed_families_Pe_1")
    @Expose
    private Object oneParentHeadedFamiliesPe1;
    /** (Required) */
    @SerializedName("Rank_One_Parent_headed_famili_1")
    @Expose
    private Object rankOneParentHeadedFamili1;
    /** (Required) */
    @SerializedName("Equivalised_median_income")
    @Expose
    private Object equivalisedMedianIncome;
    /** (Required) */
    @SerializedName("Rank_Equivalised_median_income")
    @Expose
    private Object rankEquivalisedMedianIncome;
    /** (Required) */
    @SerializedName("Delayed_medical_consultation_be")
    @Expose
    private Object delayedMedicalConsultationBe;
    /** (Required) */
    @SerializedName("Rank_Delayed_medical_consultati")
    @Expose
    private Object rankDelayedMedicalConsultati;
    /** (Required) */
    @SerializedName("Delayed_purchasing_prescribed_m")
    @Expose
    private Object delayedPurchasingPrescribedM;
    /** (Required) */
    @SerializedName("Rank_Delayed_purchasing_prescri")
    @Expose
    private Object rankDelayedPurchasingPrescri;
    /** (Required) */
    @SerializedName("Percent_low_income_welfare_depe")
    @Expose
    private Object percentLowIncomeWelfareDepe;
    /** (Required) */
    @SerializedName("Rank_Percent_low_income_welfare")
    @Expose
    private Object rankPercentLowIncomeWelfare;
    /** (Required) */
    @SerializedName("Percent_of_pop_with_food_insecu")
    @Expose
    private Object percentOfPopWithFoodInsecu;
    /** (Required) */
    @SerializedName("Rank_Percent_of_pop_with_food_i")
    @Expose
    private Object rankPercentOfPopWithFoodI;
    /** (Required) */
    @SerializedName("Percent_Mortgage_Stress")
    @Expose
    private Object percentMortgageStress;
    /** (Required) */
    @SerializedName("Rank_Percent_Mortgage_Stress")
    @Expose
    private Object rankPercentMortgageStress;
    /** (Required) */
    @SerializedName("Percent_Rental_Stress")
    @Expose
    private Object percentRentalStress;
    /** (Required) */
    @SerializedName("Rank_Percent_Rental_Stress")
    @Expose
    private Object rankPercentRentalStress;
    /** (Required) */
    @SerializedName("Percent_of_rental_housing_that_")
    @Expose
    private Object percentOfRentalHousingThat;
    /** (Required) */
    @SerializedName("Rank_Percent_of_rental_housing_")
    @Expose
    private Object rankPercentOfRentalHousing;
    /** (Required) */
    @SerializedName("Median_house_price")
    @Expose
    private Object medianHousePrice;
    /** (Required) */
    @SerializedName("Rank_Median_house_price")
    @Expose
    private Object rankMedianHousePrice;
    /** (Required) */
    @SerializedName("Median_rent_for_3_bedrooms_home")
    @Expose
    private Object medianRentFor3BedroomsHome;
    /** (Required) */
    @SerializedName("Rank_Median_rent_for_3_bedrooms")
    @Expose
    private Object rankMedianRentFor3Bedrooms;
    /** (Required) */
    @SerializedName("New_dwellings_approved_for_cons")
    @Expose
    private Object newDwellingsApprovedForCons;
    /** (Required) */
    @SerializedName("Rank_New_dwellings_approved_for")
    @Expose
    private Object rankNewDwellingsApprovedFor;
    /** (Required) */
    @SerializedName("Social_housing_stock_as_a_Perce")
    @Expose
    private Object socialHousingStockAsAPerce;
    /** (Required) */
    @SerializedName("Rank_Social_housing_stock_as_a_")
    @Expose
    private Object rankSocialHousingStockAsA;
    /** (Required) */
    @SerializedName("Number_of_Social_housing_dwelli")
    @Expose
    private Object numberOfSocialHousingDwelli;
    /** (Required) */
    @SerializedName("Rank_Number_of_Social_housing_d")
    @Expose
    private Object rankNumberOfSocialHousingD;
    /** (Required) */
    @SerializedName("Homelessness_rate_per_1000_pop")
    @Expose
    private Object homelessnessRatePer1000Pop;
    /** (Required) */
    @SerializedName("Rank_Homelessness_rate_per_1000")
    @Expose
    private Object rankHomelessnessRatePer1000;
    /** (Required) */
    @SerializedName("Percent_of_work_journeys_which_")
    @Expose
    private Object percentOfWorkJourneysWhich;
    /** (Required) */
    @SerializedName("Rank_Percent_of_work_journeys_w")
    @Expose
    private Object rankPercentOfWorkJourneysW;
    /** (Required) */
    @SerializedName("Percent_of_work_journeys_which1")
    @Expose
    private Object percentOfWorkJourneysWhich1;
    /** (Required) */
    @SerializedName("Rank_Percent_of_work_journeys_1")
    @Expose
    private Object rankPercentOfWorkJourneys1;
    /** (Required) */
    @SerializedName("Persons_with_at_least_2_hour_da")
    @Expose
    private Object personsWithAtLeast2HourDa;
    /** (Required) */
    @SerializedName("Rank_Persons_with_at_least_2_ho")
    @Expose
    private Object rankPersonsWithAtLeast2Ho;
    /** (Required) */
    @SerializedName("Percent_households_with_no_moto")
    @Expose
    private Object percentHouseholdsWithNoMoto;
    /** (Required) */
    @SerializedName("Rank_Percent_households_with_no")
    @Expose
    private Object rankPercentHouseholdsWithNo;
    /** (Required) */
    @SerializedName("FTE_Students")
    @Expose
    private Object fTEStudents;
    /** (Required) */
    @SerializedName("Percent_year_9_students_who_att")
    @Expose
    private Object percentYear9StudentsWhoAtt;
    /** (Required) */
    @SerializedName("Rank_Percent_of_year_9_students")
    @Expose
    private Object rankPercentOfYear9Students;
    /** (Required) */
    @SerializedName("Percent_of_year_9_students_who_")
    @Expose
    private Object percentOfYear9StudentsWho;
    /** (Required) */
    @SerializedName("Rank_Percent_year_9_students_wh")
    @Expose
    private Object rankPercentYear9StudentsWh;
    /** (Required) */
    @SerializedName("Percent_19_year_olds_completing")
    @Expose
    private Object percent19YearOldsCompleting;
    /** (Required) */
    @SerializedName("Rank_Percent_19_year_olds_compl")
    @Expose
    private Object rankPercent19YearOldsCompl;
    /** (Required) */
    @SerializedName("Percent_persons_who_did_not_com")
    @Expose
    private Object percentPersonsWhoDidNotCom;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_who_did")
    @Expose
    private Object rankPercentOfPersonsWhoDid;
    /** (Required) */
    @SerializedName("Percent_persons_who_completed_a")
    @Expose
    private Object percentPersonsWhoCompletedA;
    /** (Required) */
    @SerializedName("Rank_Percent_persons_who_comple")
    @Expose
    private Object rankPercentPersonsWhoComple;
    /** (Required) */
    @SerializedName("Percent_of_school_children_atte")
    @Expose
    private Object percentOfSchoolChildrenAtte;
    /** (Required) */
    @SerializedName("Rank_Percent_of_school_children")
    @Expose
    private Object rankPercentOfSchoolChildren;
    /** (Required) */
    @SerializedName("Percent_of_persons_reporting_as")
    @Expose
    private Object percentOfPersonsReportingAs;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_reporti")
    @Expose
    private Object rankPercentOfPersonsReporti;
    /** (Required) */
    @SerializedName("Percent_of_persons_reporting_ty")
    @Expose
    private Object percentOfPersonsReportingTy;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_repor_1")
    @Expose
    private Object rankPercentOfPersonsRepor1;
    /** (Required) */
    @SerializedName("Percent_of_persons_reporting_hi")
    @Expose
    private Object percentOfPersonsReportingHi;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_repor_2")
    @Expose
    private Object rankPercentOfPersonsRepor2;
    /** (Required) */
    @SerializedName("Percent_of_persons_reporting_he")
    @Expose
    private Object percentOfPersonsReportingHe;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_repor_3")
    @Expose
    private Object rankPercentOfPersonsRepor3;
    /** (Required) */
    @SerializedName("Percent_of_persons_reporting_os")
    @Expose
    private Object percentOfPersonsReportingOs;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_repor_4")
    @Expose
    private Object rankPercentOfPersonsRepor4;
    /** (Required) */
    @SerializedName("Percent_of_persons_reporting_ar")
    @Expose
    private Object percentOfPersonsReportingAr;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_repor_5")
    @Expose
    private Object rankPercentOfPersonsRepor5;
    /** (Required) */
    @SerializedName("Percent_of_persons_who_are_over")
    @Expose
    private Object percentOfPersonsWhoAreOver;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_who_are")
    @Expose
    private Object rankPercentOfPersonsWhoAre;
    /** (Required) */
    @SerializedName("Percent_of_females_who_are_over")
    @Expose
    private Object percentOfFemalesWhoAreOver;
    /** (Required) */
    @SerializedName("Rank_Percent_of_females_who_are")
    @Expose
    private Object rankPercentOfFemalesWhoAre;
    /** (Required) */
    @SerializedName("Percent_of_males_who_are_overwe")
    @Expose
    private Object percentOfMalesWhoAreOverwe;
    /** (Required) */
    @SerializedName("Rank_Percent_of_males_who_are_o")
    @Expose
    private Object rankPercentOfMalesWhoAreO;
    /** (Required) */
    @SerializedName("Percent_of_persons_who_are_obes")
    @Expose
    private Object percentOfPersonsWhoAreObes;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_who_a_1")
    @Expose
    private Object rankPercentOfPersonsWhoA1;
    /** (Required) */
    @SerializedName("Percent_of_females_who_are_obes")
    @Expose
    private Object percentOfFemalesWhoAreObes;
    /** (Required) */
    @SerializedName("Rank_Percent_of_females_who_a_1")
    @Expose
    private Object rankPercentOfFemalesWhoA1;
    /** (Required) */
    @SerializedName("Percent_of_males_who_are_obese")
    @Expose
    private Object percentOfMalesWhoAreObese;
    /** (Required) */
    @SerializedName("Rank_Percent_of_males_who_are_1")
    @Expose
    private Object rankPercentOfMalesWhoAre1;
    /** (Required) */
    @SerializedName("Malignant_cancers_diagnosed_per")
    @Expose
    private Object malignantCancersDiagnosedPer;
    /** (Required) */
    @SerializedName("Rank_Malignant_cancers_diagnose")
    @Expose
    private Object rankMalignantCancersDiagnose;
    /** (Required) */
    @SerializedName("Male_Cancer_incidence_per_1000_")
    @Expose
    private Object maleCancerIncidencePer1000;
    /** (Required) */
    @SerializedName("Rank_Male_Cancer_incidence_per_")
    @Expose
    private Object rankMaleCancerIncidencePer;
    /** (Required) */
    @SerializedName("Female_Cancer_incidence_per_100")
    @Expose
    private Object femaleCancerIncidencePer100;
    /** (Required) */
    @SerializedName("Rank_Female_Cancer_incidence_pe")
    @Expose
    private Object rankFemaleCancerIncidencePe;
    /** (Required) */
    @SerializedName("Percent_Poor_dental_health")
    @Expose
    private Object percentPoorDentalHealth;
    /** (Required) */
    @SerializedName("Rank_Percent_Poor_dental_health")
    @Expose
    private Object rankPercentPoorDentalHealth;
    /** (Required) */
    @SerializedName("Notifications_per_100000_pop_of")
    @Expose
    private Object notificationsPer100000PopOf;
    /** (Required) */
    @SerializedName("Rank_Notifications_per_100000_p")
    @Expose
    private Object rankNotificationsPer100000P;
    /** (Required) */
    @SerializedName("Notifications_per_100000_pop__1")
    @Expose
    private Object notificationsPer100000Pop1;
    /** (Required) */
    @SerializedName("Rank_Notifications_per_100000_1")
    @Expose
    private Object rankNotificationsPer1000001;
    /** (Required) */
    @SerializedName("Notifications_per_100000_people")
    @Expose
    private Object notificationsPer100000People;
    /** (Required) */
    @SerializedName("Rank_Notifications_per_100000_2")
    @Expose
    private Object rankNotificationsPer1000002;
    /** (Required) */
    @SerializedName("Percent_of_persons_18yrsPlus_wh")
    @Expose
    private Object percentOfPersons18yrsPlusWh;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_18yrsPl")
    @Expose
    private Object rankPercentOfPersons18yrsPl;
    /** (Required) */
    @SerializedName("Percent_of_males_18yrsPlus_who_")
    @Expose
    private Object percentOfMales18yrsPlusWho;
    /** (Required) */
    @SerializedName("Rank_Percent_of_males_18yrsPlus")
    @Expose
    private Object rankPercentOfMales18yrsPlus;
    /** (Required) */
    @SerializedName("Percent_of_females_18yrsPlus_wh")
    @Expose
    private Object percentOfFemales18yrsPlusWh;
    /** (Required) */
    @SerializedName("Rank_Percent_of_females_18yrsPl")
    @Expose
    private Object rankPercentOfFemales18yrsPl;
    /** (Required) */
    @SerializedName("Consumed_alcohol_at_least_weekl")
    @Expose
    private Object consumedAlcoholAtLeastWeekl;
    /** (Required) */
    @SerializedName("Rank_Consumed_alcohol_at_least_")
    @Expose
    private Object rankConsumedAlcoholAtLeast;
    /** (Required) */
    @SerializedName("Consumed_alcohol_at_least_wee_1")
    @Expose
    private Object consumedAlcoholAtLeastWee1;
    /** (Required) */
    @SerializedName("Rank_Consumed_alcohol_at_least1")
    @Expose
    private Object rankConsumedAlcoholAtLeast1;
    /** (Required) */
    @SerializedName("Percent_of_persons_who_do_not_m")
    @Expose
    private Object percentOfPersonsWhoDoNotM;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_who_do_")
    @Expose
    private Object rankPercentOfPersonsWhoDo;
    /** (Required) */
    @SerializedName("Percent_of_males_who_do_not_mee")
    @Expose
    private Object percentOfMalesWhoDoNotMee;
    /** (Required) */
    @SerializedName("Rank_Percent_of_males_who_do_no")
    @Expose
    private Object rankPercentOfMalesWhoDoNo;
    /** (Required) */
    @SerializedName("Percent_of_females_who_do_not_m")
    @Expose
    private Object percentOfFemalesWhoDoNotM;
    /** (Required) */
    @SerializedName("Rank_Percent_of_females_who_do_")
    @Expose
    private Object rankPercentOfFemalesWhoDo;
    /** (Required) */
    @SerializedName("Percent_of_persons_who_drink_so")
    @Expose
    private Object percentOfPersonsWhoDrinkSo;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_who_dri")
    @Expose
    private Object rankPercentOfPersonsWhoDri;
    /** (Required) */
    @SerializedName("Percent_of_persons_who_share_a_")
    @Expose
    private Object percentOfPersonsWhoShareA;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_who_sha")
    @Expose
    private Object rankPercentOfPersonsWhoSha;
    /** (Required) */
    @SerializedName("Percent_of_persons_who_do_not_1")
    @Expose
    private Object percentOfPersonsWhoDoNot1;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_who_do1")
    @Expose
    private Object rankPercentOfPersonsWhoDo1;
    /** (Required) */
    @SerializedName("Percent_of_males_who_do_not_m_1")
    @Expose
    private Object percentOfMalesWhoDoNotM1;
    /** (Required) */
    @SerializedName("Rank_Percent_of_males_who_do__1")
    @Expose
    private Object rankPercentOfMalesWhoDo1;
    /** (Required) */
    @SerializedName("Percent_of_females_who_do_not_1")
    @Expose
    private Object percentOfFemalesWhoDoNot1;
    /** (Required) */
    @SerializedName("Rank_Percent_of_females_who_do1")
    @Expose
    private Object rankPercentOfFemalesWhoDo1;
    /** (Required) */
    @SerializedName("Percent_of_persons_who_sit_for_")
    @Expose
    private Object percentOfPersonsWhoSitFor;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_who_sit")
    @Expose
    private Object rankPercentOfPersonsWhoSit;
    /** (Required) */
    @SerializedName("Percent_of_persons_who_visit_a_")
    @Expose
    private Object percentOfPersonsWhoVisitA;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_who_vis")
    @Expose
    private Object rankPercentOfPersonsWhoVis;
    /** (Required) */
    @SerializedName("Percent_of_breast_screening_par")
    @Expose
    private Object percentOfBreastScreeningPar;
    /** (Required) */
    @SerializedName("Rank_Percent_of_breast_screenin")
    @Expose
    private Object rankPercentOfBreastScreenin;
    /** (Required) */
    @SerializedName("Percent_of_cervical_cancer_scre")
    @Expose
    private Object percentOfCervicalCancerScre;
    /** (Required) */
    @SerializedName("Rank_Percent_of_cervical_cancer")
    @Expose
    private Object rankPercentOfCervicalCancer;
    /** (Required) */
    @SerializedName("Bowel_cancer_screening_particip")
    @Expose
    private Object bowelCancerScreeningParticip;
    /** (Required) */
    @SerializedName("Rank_Bowel_cancer_screening_par")
    @Expose
    private Object rankBowelCancerScreeningPar;
    /** (Required) */
    @SerializedName("Bowel_cancer_screening_partic_1")
    @Expose
    private Object bowelCancerScreeningPartic1;
    /** (Required) */
    @SerializedName("Rank_Bowel_cancer_screening_p_1")
    @Expose
    private Object rankBowelCancerScreeningP1;
    /** (Required) */
    @SerializedName("Bowel_cancer_screening_partic_2")
    @Expose
    private Object bowelCancerScreeningPartic2;
    /** (Required) */
    @SerializedName("Rank_Bowel_cancer_screening_p_2")
    @Expose
    private Object rankBowelCancerScreeningP2;
    /** (Required) */
    @SerializedName("Low_Birthweight_babies")
    @Expose
    private Object lowBirthweightBabies;
    /** (Required) */
    @SerializedName("Rank_Low_Birthweight_babies")
    @Expose
    private Object rankLowBirthweightBabies;
    /** (Required) */
    @SerializedName("Percent_Infants_fully_breastfed")
    @Expose
    private Object percentInfantsFullyBreastfed;
    /** (Required) */
    @SerializedName("Rank_Percent_Infants_fully_brea")
    @Expose
    private Object rankPercentInfantsFullyBrea;
    /** (Required) */
    @SerializedName("Percent_Children_fully_immunise")
    @Expose
    private Object percentChildrenFullyImmunise;
    /** (Required) */
    @SerializedName("Rank_Percent_Children_fully_imm")
    @Expose
    private Object rankPercentChildrenFullyImm;
    /** (Required) */
    @SerializedName("Proportion_of_infants_enrolled_")
    @Expose
    private Object proportionOfInfantsEnrolled;
    /** (Required) */
    @SerializedName("Rank_Proportion_of_infants_enro")
    @Expose
    private Object rankProportionOfInfantsEnro;
    /** (Required) */
    @SerializedName("Kindergarten_participation_rate")
    @Expose
    private Object kindergartenParticipationRate;
    /** (Required) */
    @SerializedName("Percent_of_children_with_kinder")
    @Expose
    private Object percentOfChildrenWithKinder;
    /** (Required) */
    @SerializedName("Rank_Percent_of_children_with_k")
    @Expose
    private Object rankPercentOfChildrenWithK;
    /** (Required) */
    @SerializedName("Percent_of_children_with_emotio")
    @Expose
    private Object percentOfChildrenWithEmotio;
    /** (Required) */
    @SerializedName("Rank_Percent_of_children_with_e")
    @Expose
    private Object rankPercentOfChildrenWithE;
    /** (Required) */
    @SerializedName("Percent_of_children_with_speech")
    @Expose
    private Object percentOfChildrenWithSpeech;
    /** (Required) */
    @SerializedName("Rank_Percent_of_children_with_s")
    @Expose
    private Object rankPercentOfChildrenWithS;
    /** (Required) */
    @SerializedName("Percent_of_adolescents_who_repo")
    @Expose
    private Object percentOfAdolescentsWhoRepo;
    /** (Required) */
    @SerializedName("Rank_Percent_of_adolescents_who")
    @Expose
    private Object rankPercentOfAdolescentsWho;
    /** (Required) */
    @SerializedName("Percent_of_children_who_are_dev")
    @Expose
    private Object percentOfChildrenWhoAreDev;
    /** (Required) */
    @SerializedName("Rank_Percent_of_children_who_ar")
    @Expose
    private Object rankPercentOfChildrenWhoAr;
    /** (Required) */
    @SerializedName("Percent_of_children_who_are_d_1")
    @Expose
    private Object percentOfChildrenWhoAreD1;
    /** (Required) */
    @SerializedName("Rank_Percent_of_children_who__1")
    @Expose
    private Object rankPercentOfChildrenWho1;
    /** (Required) */
    @SerializedName("Core_activity_need_for_assistan")
    @Expose
    private Object coreActivityNeedForAssistan;
    /** (Required) */
    @SerializedName("Rank_Core_activity_need_for_ass")
    @Expose
    private Object rankCoreActivityNeedForAss;
    /** (Required) */
    @SerializedName("People_with_severe_and_profound")
    @Expose
    private Object peopleWithSevereAndProfound;
    /** (Required) */
    @SerializedName("Rank_People_with_severe_and_pro")
    @Expose
    private Object rankPeopleWithSevereAndPro;
    /** (Required) */
    @SerializedName("People_with_severe_and_profou_1")
    @Expose
    private Object peopleWithSevereAndProfou1;
    /** (Required) */
    @SerializedName("Rank_People_with_severe_and_p_1")
    @Expose
    private Object rankPeopleWithSevereAndP1;
    /** (Required) */
    @SerializedName("Percent_pop_aged_75yrsPlus_livi")
    @Expose
    private Object percentPopAged75yrsPlusLivi;
    /** (Required) */
    @SerializedName("Rank_Percent_pop_aged_75yrsPlus")
    @Expose
    private Object rankPercentPopAged75yrsPlus;
    /** (Required) */
    @SerializedName("Pop_aged_75yrsPlus_living_alone")
    @Expose
    private Object popAged75yrsPlusLivingAlone;
    /** (Required) */
    @SerializedName("Rank_Pop_aged_75yrsPlus_living_")
    @Expose
    private Object rankPopAged75yrsPlusLiving;
    /** (Required) */
    @SerializedName("Pop_aged_75yrsPlus_living_alo_1")
    @Expose
    private Object popAged75yrsPlusLivingAlo1;
    /** (Required) */
    @SerializedName("Rank_Pop_aged_75yrsPlus_living1")
    @Expose
    private Object rankPopAged75yrsPlusLiving1;
    /** (Required) */
    @SerializedName("Persons_receiving_Disability_Se")
    @Expose
    private Object personsReceivingDisabilitySe;
    /** (Required) */
    @SerializedName("Rank_Persons_receiving_Disabili")
    @Expose
    private Object rankPersonsReceivingDisabili;
    /** (Required) */
    @SerializedName("Disability_pension_per_1000_eli")
    @Expose
    private Object disabilityPensionPer1000Eli;
    /** (Required) */
    @SerializedName("Rank_Disability_pension_per_100")
    @Expose
    private Object rankDisabilityPensionPer100;
    /** (Required) */
    @SerializedName("Aged_care_HighCare_beds")
    @Expose
    private Object agedCareHighCareBeds;
    /** (Required) */
    @SerializedName("Aged_care_LowCare_beds")
    @Expose
    private Object agedCareLowCareBeds;
    /** (Required) */
    @SerializedName("Age_pension_per_1000_eligible_p")
    @Expose
    private Object agePensionPer1000EligibleP;
    /** (Required) */
    @SerializedName("Rank_Age_pension_per_1000_eligi")
    @Expose
    private Object rankAgePensionPer1000Eligi;
    /** (Required) */
    @SerializedName("Male_life_expectancy")
    @Expose
    private Object maleLifeExpectancy;
    /** (Required) */
    @SerializedName("Rank_Male_life_expectancy")
    @Expose
    private Object rankMaleLifeExpectancy;
    /** (Required) */
    @SerializedName("Female_life_expectancy")
    @Expose
    private Object femaleLifeExpectancy;
    /** (Required) */
    @SerializedName("Rank_Female_life_expectancy")
    @Expose
    private Object rankFemaleLifeExpectancy;
    /** (Required) */
    @SerializedName("Persons_reporting_fair_or_poor_")
    @Expose
    private Object personsReportingFairOrPoor;
    /** (Required) */
    @SerializedName("Rank_Persons_reporting_fair_or_")
    @Expose
    private Object rankPersonsReportingFairOr;
    /** (Required) */
    @SerializedName("Females_reporting_fair_or_poor_")
    @Expose
    private Object femalesReportingFairOrPoor;
    /** (Required) */
    @SerializedName("Rank_Females_reporting_fair_or_")
    @Expose
    private Object rankFemalesReportingFairOr;
    /** (Required) */
    @SerializedName("Males_reporting_fair_or_poor_he")
    @Expose
    private Object malesReportingFairOrPoorHe;
    /** (Required) */
    @SerializedName("Rank_Males_reporting_fair_or_po")
    @Expose
    private Object rankMalesReportingFairOrPo;
    /** (Required) */
    @SerializedName("Percent_who_have_a_high_degree_")
    @Expose
    private Object percentWhoHaveAHighDegree;
    /** (Required) */
    @SerializedName("Rank_Percent_who_have_a_high_de")
    @Expose
    private Object rankPercentWhoHaveAHighDe;
    /** (Required) */
    @SerializedName("Percent_of_persons_sleeping_les")
    @Expose
    private Object percentOfPersonsSleepingLes;
    /** (Required) */
    @SerializedName("Rank_Percent_of_persons_sleepin")
    @Expose
    private Object rankPercentOfPersonsSleepin;
    /** (Required) */
    @SerializedName("Percent_persons_with_adequate_w")
    @Expose
    private Object percentPersonsWithAdequateW;
    /** (Required) */
    @SerializedName("Rank_Percent_persons_with_adequ")
    @Expose
    private Object rankPercentPersonsWithAdequ;
    /** (Required) */
    @SerializedName("Unintentional_injuries_treated_")
    @Expose
    private Object unintentionalInjuriesTreated;
    /** (Required) */
    @SerializedName("Rank_Unintentional_injuries_tre")
    @Expose
    private Object rankUnintentionalInjuriesTre;
    /** (Required) */
    @SerializedName("Intentional_injuries_treated_in")
    @Expose
    private Object intentionalInjuriesTreatedIn;
    /** (Required) */
    @SerializedName("Rank_Intentional_injuries_treat")
    @Expose
    private Object rankIntentionalInjuriesTreat;
    /** (Required) */
    @SerializedName("Percent_of_unintentional_hospit")
    @Expose
    private Object percentOfUnintentionalHospit;
    /** (Required) */
    @SerializedName("Rank_Percent_of_unintentional_h")
    @Expose
    private Object rankPercentOfUnintentionalH;
    /** (Required) */
    @SerializedName("Indirect_standardised_death_rat")
    @Expose
    private Object indirectStandardisedDeathRat;
    /** (Required) */
    @SerializedName("Rank_Indirect_standardised_deat")
    @Expose
    private Object rankIndirectStandardisedDeat;
    /** (Required) */
    @SerializedName("Avoidable_deaths_0to74_yrs_for_")
    @Expose
    private Object avoidableDeaths0to74YrsFor;
    /** (Required) */
    @SerializedName("Rank_Avoidable_deaths_0to74_yrs")
    @Expose
    private Object rankAvoidableDeaths0to74Yrs;
    /** (Required) */
    @SerializedName("Avoidable_deaths_0to74yrsrs_for")
    @Expose
    private Object avoidableDeaths0to74yrsrsFor;
    /** (Required) */
    @SerializedName("Rank_Avoidable_deaths_0to74yrsr")
    @Expose
    private Object rankAvoidableDeaths0to74yrsr;
    /** (Required) */
    @SerializedName("Avoidable_deaths_0to74yrsrs_f_1")
    @Expose
    private Object avoidableDeaths0to74yrsrsF1;
    /** (Required) */
    @SerializedName("Rank_Avoidable_deaths_0to74yr_1")
    @Expose
    private Object rankAvoidableDeaths0to74yr1;
    /** (Required) */
    @SerializedName("Avoidable_deaths_0to74yrs_for_r")
    @Expose
    private Object avoidableDeaths0to74yrsForR;
    /** (Required) */
    @SerializedName("Rank_Avoidable_deaths_0to74yrs_")
    @Expose
    private Object rankAvoidableDeaths0to74yrs;
    /** (Required) */
    @SerializedName("Primary_Health_Network")
    @Expose
    private Object primaryHealthNetwork;
    /** (Required) */
    @SerializedName("Primary_Care_Partnership")
    @Expose
    private Object primaryCarePartnership;
    /** (Required) */
    @SerializedName("Number_of_hospitals_and_health_")
    @Expose
    private Object numberOfHospitalsAndHealth;
    /** (Required) */
    @SerializedName("Number_of_public_hospitals_and_")
    @Expose
    private Object numberOfPublicHospitalsAnd;
    /** (Required) */
    @SerializedName("Number_of_private_hospitals_and")
    @Expose
    private Object numberOfPrivateHospitalsAnd;
    /** (Required) */
    @SerializedName("GPs_per_1000_pop")
    @Expose
    private Object gPsPer1000Pop;
    /** (Required) */
    @SerializedName("Rank_GPs_per_1000_pop")
    @Expose
    private Object rankGPsPer1000Pop;
    /** (Required) */
    @SerializedName("GP_sites_per_1000_pop")
    @Expose
    private Object gPSitesPer1000Pop;
    /** (Required) */
    @SerializedName("Rank_GP_sites_per_1000_pop")
    @Expose
    private Object rankGPSitesPer1000Pop;
    /** (Required) */
    @SerializedName("Allied_health_sites_per_1000_po")
    @Expose
    private Object alliedHealthSitesPer1000Po;
    /** (Required) */
    @SerializedName("Rank_Allied_health_sites_per_10")
    @Expose
    private Object rankAlliedHealthSitesPer10;
    /** (Required) */
    @SerializedName("Dental_services_per_1000_pop")
    @Expose
    private Object dentalServicesPer1000Pop;
    /** (Required) */
    @SerializedName("Rank_Dental_services_per_1000_p")
    @Expose
    private Object rankDentalServicesPer1000P;
    /** (Required) */
    @SerializedName("Pharmacies_per_1000_pop")
    @Expose
    private Object pharmaciesPer1000Pop;
    /** (Required) */
    @SerializedName("Rank_Pharmacies_per_1000_pop")
    @Expose
    private Object rankPharmaciesPer1000Pop;
    /** (Required) */
    @SerializedName("Percent_Near_Public_Transport")
    @Expose
    private Object percentNearPublicTransport;
    /** (Required) */
    @SerializedName("Rank_Percent_Near_Public_Transp")
    @Expose
    private Object rankPercentNearPublicTransp;
    /** (Required) */
    @SerializedName("Percent_with_private_health_ins")
    @Expose
    private Object percentWithPrivateHealthIns;
    /** (Required) */
    @SerializedName("Rank_Percent_with_private_healt")
    @Expose
    private Object rankPercentWithPrivateHealt;
    /** (Required) */
    @SerializedName("Hospital_inpatient_separations_")
    @Expose
    private Object hospitalInpatientSeparations;
    /** (Required) */
    @SerializedName("Rank_Hospital_inpatient_separat")
    @Expose
    private Object rankHospitalInpatientSeparat;
    /** (Required) */
    @SerializedName("Percent_inpatient_separations_f")
    @Expose
    private Object percentInpatientSeparationsF;
    /** (Required) */
    @SerializedName("Rank_Percent_inpatient_separati")
    @Expose
    private Object rankPercentInpatientSeparati;
    /** (Required) */
    @SerializedName("Main_public_hospital_attended_f")
    @Expose
    private Object mainPublicHospitalAttendedF;
    /** (Required) */
    @SerializedName("Main_public_hospital_attended_P")
    @Expose
    private Object mainPublicHospitalAttendedP;
    /** (Required) */
    @SerializedName("Rank_Main_public_hospital_atten")
    @Expose
    private Object rankMainPublicHospitalAtten;
    /** (Required) */
    @SerializedName("Average_length_of_stay_in_days_")
    @Expose
    private Object averageLengthOfStayInDays;
    /** (Required) */
    @SerializedName("Rank_Average_length_of_stay_in_")
    @Expose
    private Object rankAverageLengthOfStayIn;
    /** (Required) */
    @SerializedName("Average_length_of_stay_for_all_")
    @Expose
    private Object averageLengthOfStayForAll;
    /** (Required) */
    @SerializedName("Rank_Average_length_of_stay_for")
    @Expose
    private Object rankAverageLengthOfStayFor;
    /** (Required) */
    @SerializedName("Per_annum_Percent_Change_in_Sep")
    @Expose
    private Object perAnnumPercentChangeInSep;
    /** (Required) */
    @SerializedName("Rank_Per_annum_Percent_Change_i")
    @Expose
    private Object rankPerAnnumPercentChangeI;
    /** (Required) */
    @SerializedName("Per_annum_Percent_Projected_Cha")
    @Expose
    private Object perAnnumPercentProjectedCha;
    /** (Required) */
    @SerializedName("Rank_Per_annum_Percent_Projecte")
    @Expose
    private Object rankPerAnnumPercentProjecte;
    /** (Required) */
    @SerializedName("ACSCs_per_1000_pop_Total")
    @Expose
    private Object aCSCsPer1000PopTotal;
    /** (Required) */
    @SerializedName("Rank_ACSCs_per_1000_pop_Total")
    @Expose
    private Object rankACSCsPer1000PopTotal;
    /** (Required) */
    @SerializedName("ACSCs_per_1000_pop_Acute")
    @Expose
    private Object aCSCsPer1000PopAcute;
    /** (Required) */
    @SerializedName("Rank_ACSCs_per_1000_pop_Acute")
    @Expose
    private Object rankACSCsPer1000PopAcute;
    /** (Required) */
    @SerializedName("ACSCs_per_1000_pop_Chronic")
    @Expose
    private Object aCSCsPer1000PopChronic;
    /** (Required) */
    @SerializedName("Rank_ACSCs_per_1000_pop_Chronic")
    @Expose
    private Object rankACSCsPer1000PopChronic;
    /** (Required) */
    @SerializedName("ACSCs_per_1000_pop_Vaccine_prev")
    @Expose
    private Object aCSCsPer1000PopVaccinePrev;
    /** (Required) */
    @SerializedName("Rank_ACSCs_per_1000_pop_Vaccine")
    @Expose
    private Object rankACSCsPer1000PopVaccine;
    /** (Required) */
    @SerializedName("Emergency_Department_presentati")
    @Expose
    private Object emergencyDepartmentPresentati;
    /** (Required) */
    @SerializedName("Rank_Emergency_Department_prese")
    @Expose
    private Object rankEmergencyDepartmentPrese;
    /** (Required) */
    @SerializedName("Primary_care_type_presentations")
    @Expose
    private Object primaryCareTypePresentations;
    /** (Required) */
    @SerializedName("Rank_Primary_care_type_presenta")
    @Expose
    private Object rankPrimaryCareTypePresenta;
    /** (Required) */
    @SerializedName("Child_protection_investigations")
    @Expose
    private Object childProtectionInvestigations;
    /** (Required) */
    @SerializedName("Rank_Child_protection_investiga")
    @Expose
    private Object rankChildProtectionInvestiga;
    /** (Required) */
    @SerializedName("Child_protection_substantiation")
    @Expose
    private Object childProtectionSubstantiation;
    /** (Required) */
    @SerializedName("Rank_Child_protection_substanti")
    @Expose
    private Object rankChildProtectionSubstanti;
    /** (Required) */
    @SerializedName("Number_of_Child_FIRST_assessmen")
    @Expose
    private Object numberOfChildFIRSTAssessmen;
    /** (Required) */
    @SerializedName("Rank_Number_of_Child_FIRST_asse")
    @Expose
    private Object rankNumberOfChildFIRSTAsse;
    /** (Required) */
    @SerializedName("GP_attendances_per_1000_pop_Mal")
    @Expose
    private Object gPAttendancesPer1000PopMal;
    /** (Required) */
    @SerializedName("Rank_GP_attendances_per_1000_po")
    @Expose
    private Object rankGPAttendancesPer1000Po;
    /** (Required) */
    @SerializedName("GP_attendances_per_1000_pop_Fem")
    @Expose
    private Object gPAttendancesPer1000PopFem;
    /** (Required) */
    @SerializedName("Rank_GP_attendances_per_1000__1")
    @Expose
    private Object rankGPAttendancesPer10001;
    /** (Required) */
    @SerializedName("GP_attendances_per_1000_pop_Tot")
    @Expose
    private Object gPAttendancesPer1000PopTot;
    /** (Required) */
    @SerializedName("Rank_GP_attendances_per_1000__2")
    @Expose
    private Object rankGPAttendancesPer10002;
    /** (Required) */
    @SerializedName("HACC_clients_aged_0to64yrs_per_")
    @Expose
    private Object hACCClientsAged0to64yrsPer;
    /** (Required) */
    @SerializedName("Rank_HACC_clients_aged_0to64yrs")
    @Expose
    private Object rankHACCClientsAged0to64yrs;
    /** (Required) */
    @SerializedName("HACC_clients_aged_65yrsPlus_per")
    @Expose
    private Object hACCClientsAged65yrsPlusPer;
    /** (Required) */
    @SerializedName("Rank_HACC_clients_aged_65yrsPlu")
    @Expose
    private Object rankHACCClientsAged65yrsPlu;
    /** (Required) */
    @SerializedName("No_clients_who_received_Alcohol")
    @Expose
    private Object noClientsWhoReceivedAlcohol;
    /** (Required) */
    @SerializedName("Rank_No_clients_who_received_Al")
    @Expose
    private Object rankNoClientsWhoReceivedAl;
    /** (Required) */
    @SerializedName("Registered_mental_mealth_client")
    @Expose
    private Object registeredMentalMealthClient;
    /** (Required) */
    @SerializedName("Rank_Registered_mental_mealth_c")
    @Expose
    private Object rankRegisteredMentalMealthC;

    /**
     * (Required)
     *
     * @return The lGA
     */
    public Object getLGA() {
        return lGA;
    }

    /**
     * (Required)
     *
     * @param lGA The LGA
     */
    public void setLGA(Object lGA) {
        this.lGA = lGA;
    }

    /**
     * (Required)
     *
     * @return The metropolitanRural
     */
    public Object getMetropolitanRural() {
        return metropolitanRural;
    }

    /**
     * (Required)
     *
     * @param metropolitanRural The MetropolitanRural
     */
    public void setMetropolitanRural(Object metropolitanRural) {
        this.metropolitanRural = metropolitanRural;
    }

    /**
     * (Required)
     *
     * @return The departmentalRegion
     */
    public Object getDepartmentalRegion() {
        return departmentalRegion;
    }

    /**
     * (Required)
     *
     * @param departmentalRegion The Departmental_Region
     */
    public void setDepartmentalRegion(Object departmentalRegion) {
        this.departmentalRegion = departmentalRegion;
    }

    /**
     * (Required)
     *
     * @return The departmentalArea
     */
    public Object getDepartmentalArea() {
        return departmentalArea;
    }

    /**
     * (Required)
     *
     * @param departmentalArea The Departmental_Area
     */
    public void setDepartmentalArea(Object departmentalArea) {
        this.departmentalArea = departmentalArea;
    }

    /**
     * (Required)
     *
     * @return The areaOfLGASqKm
     */
    public Object getAreaOfLGASqKm() {
        return areaOfLGASqKm;
    }

    /**
     * (Required)
     *
     * @param areaOfLGASqKm The Area_of_LGA_sq_km
     */
    public void setAreaOfLGASqKm(Object areaOfLGASqKm) {
        this.areaOfLGASqKm = areaOfLGASqKm;
    }

    /**
     * (Required)
     *
     * @return The aSGSLGACode
     */
    public Object getASGSLGACode() {
        return aSGSLGACode;
    }

    /**
     * (Required)
     *
     * @param aSGSLGACode The ASGS_LGA_code
     */
    public void setASGSLGACode(Object aSGSLGACode) {
        this.aSGSLGACode = aSGSLGACode;
    }

    /**
     * (Required)
     *
     * @return The mostPopulousTownOrSuburbIn
     */
    public Object getMostPopulousTownOrSuburbIn() {
        return mostPopulousTownOrSuburbIn;
    }

    /**
     * (Required)
     *
     * @param mostPopulousTownOrSuburbIn The Most_populous_town_or_suburb_in
     */
    public void setMostPopulousTownOrSuburbIn(Object mostPopulousTownOrSuburbIn) {
        this.mostPopulousTownOrSuburbIn = mostPopulousTownOrSuburbIn;
    }

    /**
     * (Required)
     *
     * @return The distanceToMelbourneInKm
     */
    public Object getDistanceToMelbourneInKm() {
        return distanceToMelbourneInKm;
    }

    /**
     * (Required)
     *
     * @param distanceToMelbourneInKm The Distance_to_Melbourne_in_km
     */
    public void setDistanceToMelbourneInKm(Object distanceToMelbourneInKm) {
        this.distanceToMelbourneInKm = distanceToMelbourneInKm;
    }

    /**
     * (Required)
     *
     * @return The travelTimeFromMelbourneGPO
     */
    public Object getTravelTimeFromMelbourneGPO() {
        return travelTimeFromMelbourneGPO;
    }

    /**
     * (Required)
     *
     * @param travelTimeFromMelbourneGPO The Travel_time_from_Melbourne_GPO_
     */
    public void setTravelTimeFromMelbourneGPO(Object travelTimeFromMelbourneGPO) {
        this.travelTimeFromMelbourneGPO = travelTimeFromMelbourneGPO;
    }

    /**
     * (Required)
     *
     * @return The aRIARemotenessCategory
     */
    public Object getARIARemotenessCategory() {
        return aRIARemotenessCategory;
    }

    /**
     * (Required)
     *
     * @param aRIARemotenessCategory The ARIA_Remoteness_category
     */
    public void setARIARemotenessCategory(Object aRIARemotenessCategory) {
        this.aRIARemotenessCategory = aRIARemotenessCategory;
    }

    /**
     * (Required)
     *
     * @return The percentBusinessLandUse
     */
    public Object getPercentBusinessLandUse() {
        return percentBusinessLandUse;
    }

    /**
     * (Required)
     *
     * @param percentBusinessLandUse The Percent_Business_land_use
     */
    public void setPercentBusinessLandUse(Object percentBusinessLandUse) {
        this.percentBusinessLandUse = percentBusinessLandUse;
    }

    /**
     * (Required)
     *
     * @return The percentIndustrialLandUse
     */
    public Object getPercentIndustrialLandUse() {
        return percentIndustrialLandUse;
    }

    /**
     * (Required)
     *
     * @param percentIndustrialLandUse The Percent_Industrial_land_use
     */
    public void setPercentIndustrialLandUse(Object percentIndustrialLandUse) {
        this.percentIndustrialLandUse = percentIndustrialLandUse;
    }

    /**
     * (Required)
     *
     * @return The percentResidentialLandUse
     */
    public Object getPercentResidentialLandUse() {
        return percentResidentialLandUse;
    }

    /**
     * (Required)
     *
     * @param percentResidentialLandUse The Percent_Residential_land_use
     */
    public void setPercentResidentialLandUse(Object percentResidentialLandUse) {
        this.percentResidentialLandUse = percentResidentialLandUse;
    }

    /**
     * (Required)
     *
     * @return The percentRuralLandUse
     */
    public Object getPercentRuralLandUse() {
        return percentRuralLandUse;
    }

    /**
     * (Required)
     *
     * @param percentRuralLandUse The Percent_Rural_land_use
     */
    public void setPercentRuralLandUse(Object percentRuralLandUse) {
        this.percentRuralLandUse = percentRuralLandUse;
    }

    /**
     * (Required)
     *
     * @return The percentOtherLandUse
     */
    public Object getPercentOtherLandUse() {
        return percentOtherLandUse;
    }

    /**
     * (Required)
     *
     * @param percentOtherLandUse The Percent_Other_land_use
     */
    public void setPercentOtherLandUse(Object percentOtherLandUse) {
        this.percentOtherLandUse = percentOtherLandUse;
    }

    /**
     * (Required)
     *
     * @return The perAnnumPopChangeActualFor
     */
    public Object getPerAnnumPopChangeActualFor() {
        return perAnnumPopChangeActualFor;
    }

    /**
     * (Required)
     *
     * @param perAnnumPopChangeActualFor The Per_annum_pop_change_actual_for
     */
    public void setPerAnnumPopChangeActualFor(Object perAnnumPopChangeActualFor) {
        this.perAnnumPopChangeActualFor = perAnnumPopChangeActualFor;
    }

    /**
     * (Required)
     *
     * @return The perAnnumPopChangeProjected
     */
    public Object getPerAnnumPopChangeProjected() {
        return perAnnumPopChangeProjected;
    }

    /**
     * (Required)
     *
     * @param perAnnumPopChangeProjected The Per_annum_pop_change_projected_
     */
    public void setPerAnnumPopChangeProjected(Object perAnnumPopChangeProjected) {
        this.perAnnumPopChangeProjected = perAnnumPopChangeProjected;
    }

    /**
     * (Required)
     *
     * @return The females0to14yrs
     */
    public Object getFemales0to14yrs() {
        return females0to14yrs;
    }

    /**
     * (Required)
     *
     * @param females0to14yrs The Females0to14yrs
     */
    public void setFemales0to14yrs(Object females0to14yrs) {
        this.females0to14yrs = females0to14yrs;
    }

    /**
     * (Required)
     *
     * @return The females15to24yrs
     */
    public Object getFemales15to24yrs() {
        return females15to24yrs;
    }

    /**
     * (Required)
     *
     * @param females15to24yrs The Females15to24yrs
     */
    public void setFemales15to24yrs(Object females15to24yrs) {
        this.females15to24yrs = females15to24yrs;
    }

    /**
     * (Required)
     *
     * @return The females25to44yrs
     */
    public Object getFemales25to44yrs() {
        return females25to44yrs;
    }

    /**
     * (Required)
     *
     * @param females25to44yrs The Females25to44yrs
     */
    public void setFemales25to44yrs(Object females25to44yrs) {
        this.females25to44yrs = females25to44yrs;
    }

    /**
     * (Required)
     *
     * @return The females45to64yrs
     */
    public Object getFemales45to64yrs() {
        return females45to64yrs;
    }

    /**
     * (Required)
     *
     * @param females45to64yrs The Females45to64yrs
     */
    public void setFemales45to64yrs(Object females45to64yrs) {
        this.females45to64yrs = females45to64yrs;
    }

    /**
     * (Required)
     *
     * @return The females65to84yrs
     */
    public Object getFemales65to84yrs() {
        return females65to84yrs;
    }

    /**
     * (Required)
     *
     * @param females65to84yrs The Females65to84yrs
     */
    public void setFemales65to84yrs(Object females65to84yrs) {
        this.females65to84yrs = females65to84yrs;
    }

    /**
     * (Required)
     *
     * @return The female85yrsPlus
     */
    public Object getFemale85yrsPlus() {
        return female85yrsPlus;
    }

    /**
     * (Required)
     *
     * @param female85yrsPlus The Female85yrsPlus
     */
    public void setFemale85yrsPlus(Object female85yrsPlus) {
        this.female85yrsPlus = female85yrsPlus;
    }

    /**
     * (Required)
     *
     * @return The totalFemales
     */
    public Object getTotalFemales() {
        return totalFemales;
    }

    /**
     * (Required)
     *
     * @param totalFemales The TotalFemales
     */
    public void setTotalFemales(Object totalFemales) {
        this.totalFemales = totalFemales;
    }

    /**
     * (Required)
     *
     * @return The males0to14yrs
     */
    public Object getMales0to14yrs() {
        return males0to14yrs;
    }

    /**
     * (Required)
     *
     * @param males0to14yrs The Males0to14yrs
     */
    public void setMales0to14yrs(Object males0to14yrs) {
        this.males0to14yrs = males0to14yrs;
    }

    /**
     * (Required)
     *
     * @return The males15to24yrs
     */
    public Object getMales15to24yrs() {
        return males15to24yrs;
    }

    /**
     * (Required)
     *
     * @param males15to24yrs The Males15to24yrs
     */
    public void setMales15to24yrs(Object males15to24yrs) {
        this.males15to24yrs = males15to24yrs;
    }

    /**
     * (Required)
     *
     * @return The males25to44yrs
     */
    public Object getMales25to44yrs() {
        return males25to44yrs;
    }

    /**
     * (Required)
     *
     * @param males25to44yrs The Males25to44yrs
     */
    public void setMales25to44yrs(Object males25to44yrs) {
        this.males25to44yrs = males25to44yrs;
    }

    /**
     * (Required)
     *
     * @return The males45to64yrs
     */
    public Object getMales45to64yrs() {
        return males45to64yrs;
    }

    /**
     * (Required)
     *
     * @param males45to64yrs The Males45to64yrs
     */
    public void setMales45to64yrs(Object males45to64yrs) {
        this.males45to64yrs = males45to64yrs;
    }

    /**
     * (Required)
     *
     * @return The males65to84yrs
     */
    public Object getMales65to84yrs() {
        return males65to84yrs;
    }

    /**
     * (Required)
     *
     * @param males65to84yrs The Males65to84yrs
     */
    public void setMales65to84yrs(Object males65to84yrs) {
        this.males65to84yrs = males65to84yrs;
    }

    /**
     * (Required)
     *
     * @return The male85yrsPlus
     */
    public Object getMale85yrsPlus() {
        return male85yrsPlus;
    }

    /**
     * (Required)
     *
     * @param male85yrsPlus The Male85yrsPlus
     */
    public void setMale85yrsPlus(Object male85yrsPlus) {
        this.male85yrsPlus = male85yrsPlus;
    }

    /**
     * (Required)
     *
     * @return The totalMales
     */
    public Object getTotalMales() {
        return totalMales;
    }

    /**
     * (Required)
     *
     * @param totalMales The TotalMales
     */
    public void setTotalMales(Object totalMales) {
        this.totalMales = totalMales;
    }

    /**
     * (Required)
     *
     * @return The total0to14yrs
     */
    public Object getTotal0to14yrs() {
        return total0to14yrs;
    }

    /**
     * (Required)
     *
     * @param total0to14yrs The Total0to14yrs
     */
    public void setTotal0to14yrs(Object total0to14yrs) {
        this.total0to14yrs = total0to14yrs;
    }

    /**
     * (Required)
     *
     * @return The tota15to24yrs
     */
    public Object getTota15to24yrs() {
        return tota15to24yrs;
    }

    /**
     * (Required)
     *
     * @param tota15to24yrs The Tota15to24yrs
     */
    public void setTota15to24yrs(Object tota15to24yrs) {
        this.tota15to24yrs = tota15to24yrs;
    }

    /**
     * (Required)
     *
     * @return The tota25to44yrs
     */
    public Object getTota25to44yrs() {
        return tota25to44yrs;
    }

    /**
     * (Required)
     *
     * @param tota25to44yrs The Tota25to44yrs
     */
    public void setTota25to44yrs(Object tota25to44yrs) {
        this.tota25to44yrs = tota25to44yrs;
    }

    /**
     * (Required)
     *
     * @return The tota45to64yrs
     */
    public Object getTota45to64yrs() {
        return tota45to64yrs;
    }

    /**
     * (Required)
     *
     * @param tota45to64yrs The Tota45to64yrs
     */
    public void setTota45to64yrs(Object tota45to64yrs) {
        this.tota45to64yrs = tota45to64yrs;
    }

    /**
     * (Required)
     *
     * @return The tota65to84yrs
     */
    public Object getTota65to84yrs() {
        return tota65to84yrs;
    }

    /**
     * (Required)
     *
     * @param tota65to84yrs The Tota65to84yrs
     */
    public void setTota65to84yrs(Object tota65to84yrs) {
        this.tota65to84yrs = tota65to84yrs;
    }

    /**
     * (Required)
     *
     * @return The tota85yrsPlus
     */
    public Object getTota85yrsPlus() {
        return tota85yrsPlus;
    }

    /**
     * (Required)
     *
     * @param tota85yrsPlus The Tota85yrsPlus
     */
    public void setTota85yrsPlus(Object tota85yrsPlus) {
        this.tota85yrsPlus = tota85yrsPlus;
    }

    /**
     * (Required)
     *
     * @return The total2013ERP
     */
    public Object getTotal2013ERP() {
        return total2013ERP;
    }

    /**
     * (Required)
     *
     * @param total2013ERP The Total2013ERP
     */
    public void setTotal2013ERP(Object total2013ERP) {
        this.total2013ERP = total2013ERP;
    }

    /**
     * (Required)
     *
     * @return The percentTotal0to14yrs
     */
    public Object getPercentTotal0to14yrs() {
        return percentTotal0to14yrs;
    }

    /**
     * (Required)
     *
     * @param percentTotal0to14yrs The Percent_Total0to14yrs
     */
    public void setPercentTotal0to14yrs(Object percentTotal0to14yrs) {
        this.percentTotal0to14yrs = percentTotal0to14yrs;
    }

    /**
     * (Required)
     *
     * @return The percentTotal15to24yrs
     */
    public Object getPercentTotal15to24yrs() {
        return percentTotal15to24yrs;
    }

    /**
     * (Required)
     *
     * @param percentTotal15to24yrs The PercentTotal15to24yrs
     */
    public void setPercentTotal15to24yrs(Object percentTotal15to24yrs) {
        this.percentTotal15to24yrs = percentTotal15to24yrs;
    }

    /**
     * (Required)
     *
     * @return The percentTotal25to44yrs
     */
    public Object getPercentTotal25to44yrs() {
        return percentTotal25to44yrs;
    }

    /**
     * (Required)
     *
     * @param percentTotal25to44yrs The PercentTotal25to44yrs
     */
    public void setPercentTotal25to44yrs(Object percentTotal25to44yrs) {
        this.percentTotal25to44yrs = percentTotal25to44yrs;
    }

    /**
     * (Required)
     *
     * @return The percentTotal45to64yrs
     */
    public Object getPercentTotal45to64yrs() {
        return percentTotal45to64yrs;
    }

    /**
     * (Required)
     *
     * @param percentTotal45to64yrs The PercentTotal45to64yrs
     */
    public void setPercentTotal45to64yrs(Object percentTotal45to64yrs) {
        this.percentTotal45to64yrs = percentTotal45to64yrs;
    }

    /**
     * (Required)
     *
     * @return The percentTotal65to84yrs
     */
    public Object getPercentTotal65to84yrs() {
        return percentTotal65to84yrs;
    }

    /**
     * (Required)
     *
     * @param percentTotal65to84yrs The PercentTotal65to84yrs
     */
    public void setPercentTotal65to84yrs(Object percentTotal65to84yrs) {
        this.percentTotal65to84yrs = percentTotal65to84yrs;
    }

    /**
     * (Required)
     *
     * @return The percentTotal84yrsPlus
     */
    public Object getPercentTotal84yrsPlus() {
        return percentTotal84yrsPlus;
    }

    /**
     * (Required)
     *
     * @param percentTotal84yrsPlus The PercentTotal84yrsPlus
     */
    public void setPercentTotal84yrsPlus(Object percentTotal84yrsPlus) {
        this.percentTotal84yrsPlus = percentTotal84yrsPlus;
    }

    /**
     * (Required)
     *
     * @return The totalFertilityRate2012
     */
    public Object getTotalFertilityRate2012() {
        return totalFertilityRate2012;
    }

    /**
     * (Required)
     *
     * @param totalFertilityRate2012 The Total_fertility_rate_2012
     */
    public void setTotalFertilityRate2012(Object totalFertilityRate2012) {
        this.totalFertilityRate2012 = totalFertilityRate2012;
    }

    /**
     * (Required)
     *
     * @return The rankTotalFertilityRate2012
     */
    public Object getRankTotalFertilityRate2012() {
        return rankTotalFertilityRate2012;
    }

    /**
     * (Required)
     *
     * @param rankTotalFertilityRate2012 The Rank_Total_fertility_rate_2012
     */
    public void setRankTotalFertilityRate2012(Object rankTotalFertilityRate2012) {
        this.rankTotalFertilityRate2012 = rankTotalFertilityRate2012;
    }

    /**
     * (Required)
     *
     * @return The teenageFertilityRate2012
     */
    public Object getTeenageFertilityRate2012() {
        return teenageFertilityRate2012;
    }

    /**
     * (Required)
     *
     * @param teenageFertilityRate2012 The Teenage_fertility_rate_2012
     */
    public void setTeenageFertilityRate2012(Object teenageFertilityRate2012) {
        this.teenageFertilityRate2012 = teenageFertilityRate2012;
    }

    /**
     * (Required)
     *
     * @return The rankTeenageFertilityRate201
     */
    public Object getRankTeenageFertilityRate201() {
        return rankTeenageFertilityRate201;
    }

    /**
     * (Required)
     *
     * @param rankTeenageFertilityRate201 The Rank_Teenage_fertility_rate_201
     */
    public void setRankTeenageFertilityRate201(Object rankTeenageFertilityRate201) {
        this.rankTeenageFertilityRate201 = rankTeenageFertilityRate201;
    }

    /**
     * (Required)
     *
     * @return The percentAboriginalOrTorresSt
     */
    public Object getPercentAboriginalOrTorresSt() {
        return percentAboriginalOrTorresSt;
    }

    /**
     * (Required)
     *
     * @param percentAboriginalOrTorresSt The Percent_Aboriginal_or_Torres_St
     */
    public void setPercentAboriginalOrTorresSt(Object percentAboriginalOrTorresSt) {
        this.percentAboriginalOrTorresSt = percentAboriginalOrTorresSt;
    }

    /**
     * (Required)
     *
     * @return The rankAboriginalOrTorresStrai
     */
    public Object getRankAboriginalOrTorresStrai() {
        return rankAboriginalOrTorresStrai;
    }

    /**
     * (Required)
     *
     * @param rankAboriginalOrTorresStrai The Rank_Aboriginal_or_Torres_Strai
     */
    public void setRankAboriginalOrTorresStrai(Object rankAboriginalOrTorresStrai) {
        this.rankAboriginalOrTorresStrai = rankAboriginalOrTorresStrai;
    }

    /**
     * (Required)
     *
     * @return The percentBornOverseas2011
     */
    public Object getPercentBornOverseas2011() {
        return percentBornOverseas2011;
    }

    /**
     * (Required)
     *
     * @param percentBornOverseas2011 The Percent_Born_overseas_2011
     */
    public void setPercentBornOverseas2011(Object percentBornOverseas2011) {
        this.percentBornOverseas2011 = percentBornOverseas2011;
    }

    /**
     * (Required)
     *
     * @return The rankPercentBornOverseas2011
     */
    public Object getRankPercentBornOverseas2011() {
        return rankPercentBornOverseas2011;
    }

    /**
     * (Required)
     *
     * @param rankPercentBornOverseas2011 The Rank_Percent_Born_overseas_2011
     */
    public void setRankPercentBornOverseas2011(Object rankPercentBornOverseas2011) {
        this.rankPercentBornOverseas2011 = rankPercentBornOverseas2011;
    }

    /**
     * (Required)
     *
     * @return The bornInANonEnglishSpeaking
     */
    public Object getBornInANonEnglishSpeaking() {
        return bornInANonEnglishSpeaking;
    }

    /**
     * (Required)
     *
     * @param bornInANonEnglishSpeaking The Born_in_a_non_English_speaking_
     */
    public void setBornInANonEnglishSpeaking(Object bornInANonEnglishSpeaking) {
        this.bornInANonEnglishSpeaking = bornInANonEnglishSpeaking;
    }

    /**
     * (Required)
     *
     * @return The rankBornInANonEnglishSpea
     */
    public Object getRankBornInANonEnglishSpea() {
        return rankBornInANonEnglishSpea;
    }

    /**
     * (Required)
     *
     * @param rankBornInANonEnglishSpea The Rank_Born_in_a_non_English_spea
     */
    public void setRankBornInANonEnglishSpea(Object rankBornInANonEnglishSpea) {
        this.rankBornInANonEnglishSpea = rankBornInANonEnglishSpea;
    }

    /**
     * (Required)
     *
     * @return The country1PercentForTop5Ove
     */
    public Object getCountry1PercentForTop5Ove() {
        return country1PercentForTop5Ove;
    }

    /**
     * (Required)
     *
     * @param country1PercentForTop5Ove The Country_1_Percent_for_Top_5_ove
     */
    public void setCountry1PercentForTop5Ove(Object country1PercentForTop5Ove) {
        this.country1PercentForTop5Ove = country1PercentForTop5Ove;
    }

    /**
     * (Required)
     *
     * @return The country1ForTop5OverseasCo
     */
    public Object getCountry1ForTop5OverseasCo() {
        return country1ForTop5OverseasCo;
    }

    /**
     * (Required)
     *
     * @param country1ForTop5OverseasCo The Country_1_for_Top_5_overseas_co
     */
    public void setCountry1ForTop5OverseasCo(Object country1ForTop5OverseasCo) {
        this.country1ForTop5OverseasCo = country1ForTop5OverseasCo;
    }

    /**
     * (Required)
     *
     * @return The country2PercentForTop5Ove
     */
    public Object getCountry2PercentForTop5Ove() {
        return country2PercentForTop5Ove;
    }

    /**
     * (Required)
     *
     * @param country2PercentForTop5Ove The Country_2_Percent_for_Top_5_ove
     */
    public void setCountry2PercentForTop5Ove(Object country2PercentForTop5Ove) {
        this.country2PercentForTop5Ove = country2PercentForTop5Ove;
    }

    /**
     * (Required)
     *
     * @return The country2ForTop5OverseasCo
     */
    public Object getCountry2ForTop5OverseasCo() {
        return country2ForTop5OverseasCo;
    }

    /**
     * (Required)
     *
     * @param country2ForTop5OverseasCo The Country_2_for_Top_5_overseas_co
     */
    public void setCountry2ForTop5OverseasCo(Object country2ForTop5OverseasCo) {
        this.country2ForTop5OverseasCo = country2ForTop5OverseasCo;
    }

    /**
     * (Required)
     *
     * @return The country3PercentForTop5Ove
     */
    public Object getCountry3PercentForTop5Ove() {
        return country3PercentForTop5Ove;
    }

    /**
     * (Required)
     *
     * @param country3PercentForTop5Ove The Country_3_Percent_for_Top_5_ove
     */
    public void setCountry3PercentForTop5Ove(Object country3PercentForTop5Ove) {
        this.country3PercentForTop5Ove = country3PercentForTop5Ove;
    }

    /**
     * (Required)
     *
     * @return The country3ForTop5OverseasCo
     */
    public Object getCountry3ForTop5OverseasCo() {
        return country3ForTop5OverseasCo;
    }

    /**
     * (Required)
     *
     * @param country3ForTop5OverseasCo The Country_3_for_Top_5_overseas_co
     */
    public void setCountry3ForTop5OverseasCo(Object country3ForTop5OverseasCo) {
        this.country3ForTop5OverseasCo = country3ForTop5OverseasCo;
    }

    /**
     * (Required)
     *
     * @return The country4PercentForTop5Ove
     */
    public Object getCountry4PercentForTop5Ove() {
        return country4PercentForTop5Ove;
    }

    /**
     * (Required)
     *
     * @param country4PercentForTop5Ove The Country_4_Percent_for_Top_5_ove
     */
    public void setCountry4PercentForTop5Ove(Object country4PercentForTop5Ove) {
        this.country4PercentForTop5Ove = country4PercentForTop5Ove;
    }

    /**
     * (Required)
     *
     * @return The country4ForTop5OverseasCo
     */
    public Object getCountry4ForTop5OverseasCo() {
        return country4ForTop5OverseasCo;
    }

    /**
     * (Required)
     *
     * @param country4ForTop5OverseasCo The Country_4_for_Top_5_overseas_co
     */
    public void setCountry4ForTop5OverseasCo(Object country4ForTop5OverseasCo) {
        this.country4ForTop5OverseasCo = country4ForTop5OverseasCo;
    }

    /**
     * (Required)
     *
     * @return The country5PercentForTop5Ove
     */
    public Object getCountry5PercentForTop5Ove() {
        return country5PercentForTop5Ove;
    }

    /**
     * (Required)
     *
     * @param country5PercentForTop5Ove The Country_5_Percent_for_Top_5_ove
     */
    public void setCountry5PercentForTop5Ove(Object country5PercentForTop5Ove) {
        this.country5PercentForTop5Ove = country5PercentForTop5Ove;
    }

    /**
     * (Required)
     *
     * @return The country5ForTop5OverseasCo
     */
    public Object getCountry5ForTop5OverseasCo() {
        return country5ForTop5OverseasCo;
    }

    /**
     * (Required)
     *
     * @param country5ForTop5OverseasCo The Country_5_for_Top_5_overseas_co
     */
    public void setCountry5ForTop5OverseasCo(Object country5ForTop5OverseasCo) {
        this.country5ForTop5OverseasCo = country5ForTop5OverseasCo;
    }

    /**
     * (Required)
     *
     * @return The percentSpeaksLOTEAtHome
     */
    public Object getPercentSpeaksLOTEAtHome() {
        return percentSpeaksLOTEAtHome;
    }

    /**
     * (Required)
     *
     * @param percentSpeaksLOTEAtHome The Percent_Speaks_LOTE_at_home
     */
    public void setPercentSpeaksLOTEAtHome(Object percentSpeaksLOTEAtHome) {
        this.percentSpeaksLOTEAtHome = percentSpeaksLOTEAtHome;
    }

    /**
     * (Required)
     *
     * @return The rankPercentSpeaksLOTEAtHom
     */
    public Object getRankPercentSpeaksLOTEAtHom() {
        return rankPercentSpeaksLOTEAtHom;
    }

    /**
     * (Required)
     *
     * @param rankPercentSpeaksLOTEAtHom The Rank_Percent_Speaks_LOTE_at_hom
     */
    public void setRankPercentSpeaksLOTEAtHom(Object rankPercentSpeaksLOTEAtHom) {
        this.rankPercentSpeaksLOTEAtHom = rankPercentSpeaksLOTEAtHom;
    }

    /**
     * (Required)
     *
     * @return The country1PercentTop5Languag
     */
    public Object getCountry1PercentTop5Languag() {
        return country1PercentTop5Languag;
    }

    /**
     * (Required)
     *
     * @param country1PercentTop5Languag The Country_1_Percent_Top_5_languag
     */
    public void setCountry1PercentTop5Languag(Object country1PercentTop5Languag) {
        this.country1PercentTop5Languag = country1PercentTop5Languag;
    }

    /**
     * (Required)
     *
     * @return The country1Top5LanguagesSpoke
     */
    public Object getCountry1Top5LanguagesSpoke() {
        return country1Top5LanguagesSpoke;
    }

    /**
     * (Required)
     *
     * @param country1Top5LanguagesSpoke The Country_1_Top_5_languages_spoke
     */
    public void setCountry1Top5LanguagesSpoke(Object country1Top5LanguagesSpoke) {
        this.country1Top5LanguagesSpoke = country1Top5LanguagesSpoke;
    }

    /**
     * (Required)
     *
     * @return The country2PercentTop5Languag
     */
    public Object getCountry2PercentTop5Languag() {
        return country2PercentTop5Languag;
    }

    /**
     * (Required)
     *
     * @param country2PercentTop5Languag The Country_2_Percent_Top_5_languag
     */
    public void setCountry2PercentTop5Languag(Object country2PercentTop5Languag) {
        this.country2PercentTop5Languag = country2PercentTop5Languag;
    }

    /**
     * (Required)
     *
     * @return The country2Top5LanguagesSpoke
     */
    public Object getCountry2Top5LanguagesSpoke() {
        return country2Top5LanguagesSpoke;
    }

    /**
     * (Required)
     *
     * @param country2Top5LanguagesSpoke The Country_2_Top_5_languages_spoke
     */
    public void setCountry2Top5LanguagesSpoke(Object country2Top5LanguagesSpoke) {
        this.country2Top5LanguagesSpoke = country2Top5LanguagesSpoke;
    }

    /**
     * (Required)
     *
     * @return The country3PercentTop5Languag
     */
    public Object getCountry3PercentTop5Languag() {
        return country3PercentTop5Languag;
    }

    /**
     * (Required)
     *
     * @param country3PercentTop5Languag The Country_3_Percent_Top_5_languag
     */
    public void setCountry3PercentTop5Languag(Object country3PercentTop5Languag) {
        this.country3PercentTop5Languag = country3PercentTop5Languag;
    }

    /**
     * (Required)
     *
     * @return The country3Top5LanguagesSpoke
     */
    public Object getCountry3Top5LanguagesSpoke() {
        return country3Top5LanguagesSpoke;
    }

    /**
     * (Required)
     *
     * @param country3Top5LanguagesSpoke The Country_3_Top_5_languages_spoke
     */
    public void setCountry3Top5LanguagesSpoke(Object country3Top5LanguagesSpoke) {
        this.country3Top5LanguagesSpoke = country3Top5LanguagesSpoke;
    }

    /**
     * (Required)
     *
     * @return The country4PercentTop5Languag
     */
    public Object getCountry4PercentTop5Languag() {
        return country4PercentTop5Languag;
    }

    /**
     * (Required)
     *
     * @param country4PercentTop5Languag The Country_4_Percent_Top_5_languag
     */
    public void setCountry4PercentTop5Languag(Object country4PercentTop5Languag) {
        this.country4PercentTop5Languag = country4PercentTop5Languag;
    }

    /**
     * (Required)
     *
     * @return The country4Top5LanguagesSpoke
     */
    public Object getCountry4Top5LanguagesSpoke() {
        return country4Top5LanguagesSpoke;
    }

    /**
     * (Required)
     *
     * @param country4Top5LanguagesSpoke The Country_4_Top_5_languages_spoke
     */
    public void setCountry4Top5LanguagesSpoke(Object country4Top5LanguagesSpoke) {
        this.country4Top5LanguagesSpoke = country4Top5LanguagesSpoke;
    }

    /**
     * (Required)
     *
     * @return The country5PercentTop5Languag
     */
    public Object getCountry5PercentTop5Languag() {
        return country5PercentTop5Languag;
    }

    /**
     * (Required)
     *
     * @param country5PercentTop5Languag The Country_5_Percent_Top_5_languag
     */
    public void setCountry5PercentTop5Languag(Object country5PercentTop5Languag) {
        this.country5PercentTop5Languag = country5PercentTop5Languag;
    }

    /**
     * (Required)
     *
     * @return The country5Top5LanguagesSpoke
     */
    public Object getCountry5Top5LanguagesSpoke() {
        return country5Top5LanguagesSpoke;
    }

    /**
     * (Required)
     *
     * @param country5Top5LanguagesSpoke The Country_5_Top_5_languages_spoke
     */
    public void setCountry5Top5LanguagesSpoke(Object country5Top5LanguagesSpoke) {
        this.country5Top5LanguagesSpoke = country5Top5LanguagesSpoke;
    }

    /**
     * (Required)
     *
     * @return The percentLowEnglishProficiency
     */
    public Object getPercentLowEnglishProficiency() {
        return percentLowEnglishProficiency;
    }

    /**
     * (Required)
     *
     * @param percentLowEnglishProficiency The Percent_Low_English_proficiency
     */
    public void setPercentLowEnglishProficiency(Object percentLowEnglishProficiency) {
        this.percentLowEnglishProficiency = percentLowEnglishProficiency;
    }

    /**
     * (Required)
     *
     * @return The rankPercentLowEnglishProfic
     */
    public Object getRankPercentLowEnglishProfic() {
        return rankPercentLowEnglishProfic;
    }

    /**
     * (Required)
     *
     * @param rankPercentLowEnglishProfic The Rank_Percent_Low_English_profic
     */
    public void setRankPercentLowEnglishProfic(Object rankPercentLowEnglishProfic) {
        this.rankPercentLowEnglishProfic = rankPercentLowEnglishProfic;
    }

    /**
     * (Required)
     *
     * @return The ancestry1PercentTop5Ancest
     */
    public Object getAncestry1PercentTop5Ancest() {
        return ancestry1PercentTop5Ancest;
    }

    /**
     * (Required)
     *
     * @param ancestry1PercentTop5Ancest The Ancestry_1_Percent_Top_5_ancest
     */
    public void setAncestry1PercentTop5Ancest(Object ancestry1PercentTop5Ancest) {
        this.ancestry1PercentTop5Ancest = ancestry1PercentTop5Ancest;
    }

    /**
     * (Required)
     *
     * @return The ancestry1Top5Ancestries
     */
    public Object getAncestry1Top5Ancestries() {
        return ancestry1Top5Ancestries;
    }

    /**
     * (Required)
     *
     * @param ancestry1Top5Ancestries The Ancestry_1_Top_5_ancestries
     */
    public void setAncestry1Top5Ancestries(Object ancestry1Top5Ancestries) {
        this.ancestry1Top5Ancestries = ancestry1Top5Ancestries;
    }

    /**
     * (Required)
     *
     * @return The ancestry2PercentTop5Ancest
     */
    public Object getAncestry2PercentTop5Ancest() {
        return ancestry2PercentTop5Ancest;
    }

    /**
     * (Required)
     *
     * @param ancestry2PercentTop5Ancest The Ancestry_2_Percent_Top_5_ancest
     */
    public void setAncestry2PercentTop5Ancest(Object ancestry2PercentTop5Ancest) {
        this.ancestry2PercentTop5Ancest = ancestry2PercentTop5Ancest;
    }

    /**
     * (Required)
     *
     * @return The ancestry2Top5Ancestries
     */
    public Object getAncestry2Top5Ancestries() {
        return ancestry2Top5Ancestries;
    }

    /**
     * (Required)
     *
     * @param ancestry2Top5Ancestries The Ancestry_2_Top_5_ancestries
     */
    public void setAncestry2Top5Ancestries(Object ancestry2Top5Ancestries) {
        this.ancestry2Top5Ancestries = ancestry2Top5Ancestries;
    }

    /**
     * (Required)
     *
     * @return The ancestry3PercentTop5Ancest
     */
    public Object getAncestry3PercentTop5Ancest() {
        return ancestry3PercentTop5Ancest;
    }

    /**
     * (Required)
     *
     * @param ancestry3PercentTop5Ancest The Ancestry_3_Percent_Top_5_ancest
     */
    public void setAncestry3PercentTop5Ancest(Object ancestry3PercentTop5Ancest) {
        this.ancestry3PercentTop5Ancest = ancestry3PercentTop5Ancest;
    }

    /**
     * (Required)
     *
     * @return The ancestry3Top5Ancestries
     */
    public Object getAncestry3Top5Ancestries() {
        return ancestry3Top5Ancestries;
    }

    /**
     * (Required)
     *
     * @param ancestry3Top5Ancestries The Ancestry_3_Top_5_ancestries
     */
    public void setAncestry3Top5Ancestries(Object ancestry3Top5Ancestries) {
        this.ancestry3Top5Ancestries = ancestry3Top5Ancestries;
    }

    /**
     * (Required)
     *
     * @return The ancestry4PercentTop5Ancest
     */
    public Object getAncestry4PercentTop5Ancest() {
        return ancestry4PercentTop5Ancest;
    }

    /**
     * (Required)
     *
     * @param ancestry4PercentTop5Ancest The Ancestry_4_Percent_Top_5_ancest
     */
    public void setAncestry4PercentTop5Ancest(Object ancestry4PercentTop5Ancest) {
        this.ancestry4PercentTop5Ancest = ancestry4PercentTop5Ancest;
    }

    /**
     * (Required)
     *
     * @return The ancestry4Top5Ancestries
     */
    public Object getAncestry4Top5Ancestries() {
        return ancestry4Top5Ancestries;
    }

    /**
     * (Required)
     *
     * @param ancestry4Top5Ancestries The Ancestry_4_Top_5_ancestries
     */
    public void setAncestry4Top5Ancestries(Object ancestry4Top5Ancestries) {
        this.ancestry4Top5Ancestries = ancestry4Top5Ancestries;
    }

    /**
     * (Required)
     *
     * @return The ancestry5PercentTop5Ancest
     */
    public Object getAncestry5PercentTop5Ancest() {
        return ancestry5PercentTop5Ancest;
    }

    /**
     * (Required)
     *
     * @param ancestry5PercentTop5Ancest The Ancestry_5_Percent_Top_5_ancest
     */
    public void setAncestry5PercentTop5Ancest(Object ancestry5PercentTop5Ancest) {
        this.ancestry5PercentTop5Ancest = ancestry5PercentTop5Ancest;
    }

    /**
     * (Required)
     *
     * @return The ancestry5Top5Ancestries
     */
    public Object getAncestry5Top5Ancestries() {
        return ancestry5Top5Ancestries;
    }

    /**
     * (Required)
     *
     * @param ancestry5Top5Ancestries The Ancestry_5_Top_5_ancestries
     */
    public void setAncestry5Top5Ancestries(Object ancestry5Top5Ancestries) {
        this.ancestry5Top5Ancestries = ancestry5Top5Ancestries;
    }

    /**
     * (Required)
     *
     * @return The newSettlerArrivalsPer100000
     */
    public Object getNewSettlerArrivalsPer100000() {
        return newSettlerArrivalsPer100000;
    }

    /**
     * (Required)
     *
     * @param newSettlerArrivalsPer100000 The New_settler_arrivals_per_100000
     */
    public void setNewSettlerArrivalsPer100000(Object newSettlerArrivalsPer100000) {
        this.newSettlerArrivalsPer100000 = newSettlerArrivalsPer100000;
    }

    /**
     * (Required)
     *
     * @return The rankNewSettlerArrivalsPer1
     */
    public Object getRankNewSettlerArrivalsPer1() {
        return rankNewSettlerArrivalsPer1;
    }

    /**
     * (Required)
     *
     * @param rankNewSettlerArrivalsPer1 The Rank_New_settler_arrivals_per_1
     */
    public void setRankNewSettlerArrivalsPer1(Object rankNewSettlerArrivalsPer1) {
        this.rankNewSettlerArrivalsPer1 = rankNewSettlerArrivalsPer1;
    }

    /**
     * (Required)
     *
     * @return The humanitarianArrivalsAsAPerc
     */
    public Object getHumanitarianArrivalsAsAPerc() {
        return humanitarianArrivalsAsAPerc;
    }

    /**
     * (Required)
     *
     * @param humanitarianArrivalsAsAPerc The Humanitarian_arrivals_as_a_Perc
     */
    public void setHumanitarianArrivalsAsAPerc(Object humanitarianArrivalsAsAPerc) {
        this.humanitarianArrivalsAsAPerc = humanitarianArrivalsAsAPerc;
    }

    /**
     * (Required)
     *
     * @return The rankHumanitarianArrivalsAsA
     */
    public Object getRankHumanitarianArrivalsAsA() {
        return rankHumanitarianArrivalsAsA;
    }

    /**
     * (Required)
     *
     * @param rankHumanitarianArrivalsAsA The Rank_Humanitarian_arrivals_as_a
     */
    public void setRankHumanitarianArrivalsAsA(Object rankHumanitarianArrivalsAsA) {
        this.rankHumanitarianArrivalsAsA = rankHumanitarianArrivalsAsA;
    }

    /**
     * (Required)
     *
     * @return The communityAcceptanceOfDiverse
     */
    public Object getCommunityAcceptanceOfDiverse() {
        return communityAcceptanceOfDiverse;
    }

    /**
     * (Required)
     *
     * @param communityAcceptanceOfDiverse The Community_acceptance_of_diverse
     */
    public void setCommunityAcceptanceOfDiverse(Object communityAcceptanceOfDiverse) {
        this.communityAcceptanceOfDiverse = communityAcceptanceOfDiverse;
    }

    /**
     * (Required)
     *
     * @return The rankCommunityAcceptanceOfDi
     */
    public Object getRankCommunityAcceptanceOfDi() {
        return rankCommunityAcceptanceOfDi;
    }

    /**
     * (Required)
     *
     * @param rankCommunityAcceptanceOfDi The Rank_Community_acceptance_of_di
     */
    public void setRankCommunityAcceptanceOfDi(Object rankCommunityAcceptanceOfDi) {
        this.rankCommunityAcceptanceOfDi = rankCommunityAcceptanceOfDi;
    }

    /**
     * (Required)
     *
     * @return The proportionOfHouseholdsWithB
     */
    public Object getProportionOfHouseholdsWithB() {
        return proportionOfHouseholdsWithB;
    }

    /**
     * (Required)
     *
     * @param proportionOfHouseholdsWithB The Proportion_of_households_with_b
     */
    public void setProportionOfHouseholdsWithB(Object proportionOfHouseholdsWithB) {
        this.proportionOfHouseholdsWithB = proportionOfHouseholdsWithB;
    }

    /**
     * (Required)
     *
     * @return The householdsWithBroadbandInter
     */
    public Object getHouseholdsWithBroadbandInter() {
        return householdsWithBroadbandInter;
    }

    /**
     * (Required)
     *
     * @param householdsWithBroadbandInter The Households_with_broadband_inter
     */
    public void setHouseholdsWithBroadbandInter(Object householdsWithBroadbandInter) {
        this.householdsWithBroadbandInter = householdsWithBroadbandInter;
    }

    /**
     * (Required)
     *
     * @return The gamingMachineLossesPerHead
     */
    public Object getGamingMachineLossesPerHead() {
        return gamingMachineLossesPerHead;
    }

    /**
     * (Required)
     *
     * @param gamingMachineLossesPerHead The Gaming_machine_losses_per_head_
     */
    public void setGamingMachineLossesPerHead(Object gamingMachineLossesPerHead) {
        this.gamingMachineLossesPerHead = gamingMachineLossesPerHead;
    }

    /**
     * (Required)
     *
     * @return The rankGamingMachineLossesPer
     */
    public Object getRankGamingMachineLossesPer() {
        return rankGamingMachineLossesPer;
    }

    /**
     * (Required)
     *
     * @param rankGamingMachineLossesPer The Rank_Gaming_machine_losses_per_
     */
    public void setRankGamingMachineLossesPer(Object rankGamingMachineLossesPer) {
        this.rankGamingMachineLossesPer = rankGamingMachineLossesPer;
    }

    /**
     * (Required)
     *
     * @return The familyIncidentsPer1000Pop
     */
    public Object getFamilyIncidentsPer1000Pop() {
        return familyIncidentsPer1000Pop;
    }

    /**
     * (Required)
     *
     * @param familyIncidentsPer1000Pop The Family_Incidents_per_1000_pop
     */
    public void setFamilyIncidentsPer1000Pop(Object familyIncidentsPer1000Pop) {
        this.familyIncidentsPer1000Pop = familyIncidentsPer1000Pop;
    }

    /**
     * (Required)
     *
     * @return The rankFamilyIncidentsPer1000
     */
    public Object getRankFamilyIncidentsPer1000() {
        return rankFamilyIncidentsPer1000;
    }

    /**
     * (Required)
     *
     * @param rankFamilyIncidentsPer1000 The Rank_Family_Incidents_per_1000_
     */
    public void setRankFamilyIncidentsPer1000(Object rankFamilyIncidentsPer1000) {
        this.rankFamilyIncidentsPer1000 = rankFamilyIncidentsPer1000;
    }

    /**
     * (Required)
     *
     * @return The drugUsageAndPossessionOffen
     */
    public Object getDrugUsageAndPossessionOffen() {
        return drugUsageAndPossessionOffen;
    }

    /**
     * (Required)
     *
     * @param drugUsageAndPossessionOffen The Drug_usage_and_possession_offen
     */
    public void setDrugUsageAndPossessionOffen(Object drugUsageAndPossessionOffen) {
        this.drugUsageAndPossessionOffen = drugUsageAndPossessionOffen;
    }

    /**
     * (Required)
     *
     * @return The rankDrugUsageAndPossession
     */
    public Object getRankDrugUsageAndPossession() {
        return rankDrugUsageAndPossession;
    }

    /**
     * (Required)
     *
     * @param rankDrugUsageAndPossession The Rank_Drug_usage_and_possession_
     */
    public void setRankDrugUsageAndPossession(Object rankDrugUsageAndPossession) {
        this.rankDrugUsageAndPossession = rankDrugUsageAndPossession;
    }

    /**
     * (Required)
     *
     * @return The totalCrimePer1000Pop
     */
    public Object getTotalCrimePer1000Pop() {
        return totalCrimePer1000Pop;
    }

    /**
     * (Required)
     *
     * @param totalCrimePer1000Pop The Total_Crime_per_1000_pop
     */
    public void setTotalCrimePer1000Pop(Object totalCrimePer1000Pop) {
        this.totalCrimePer1000Pop = totalCrimePer1000Pop;
    }

    /**
     * (Required)
     *
     * @return The rankTotalCrimePer1000Pop
     */
    public Object getRankTotalCrimePer1000Pop() {
        return rankTotalCrimePer1000Pop;
    }

    /**
     * (Required)
     *
     * @param rankTotalCrimePer1000Pop The Rank_Total_Crime_per_1000_pop
     */
    public void setRankTotalCrimePer1000Pop(Object rankTotalCrimePer1000Pop) {
        this.rankTotalCrimePer1000Pop = rankTotalCrimePer1000Pop;
    }

    /**
     * (Required)
     *
     * @return The feelsSafeWalkingAloneDuring
     */
    public Object getFeelsSafeWalkingAloneDuring() {
        return feelsSafeWalkingAloneDuring;
    }

    /**
     * (Required)
     *
     * @param feelsSafeWalkingAloneDuring The Feels_safe_walking_alone_during
     */
    public void setFeelsSafeWalkingAloneDuring(Object feelsSafeWalkingAloneDuring) {
        this.feelsSafeWalkingAloneDuring = feelsSafeWalkingAloneDuring;
    }

    /**
     * (Required)
     *
     * @return The rankFeelsSafeWalkingAloneD
     */
    public Object getRankFeelsSafeWalkingAloneD() {
        return rankFeelsSafeWalkingAloneD;
    }

    /**
     * (Required)
     *
     * @param rankFeelsSafeWalkingAloneD The Rank_Feels_safe_walking_alone_d
     */
    public void setRankFeelsSafeWalkingAloneD(Object rankFeelsSafeWalkingAloneD) {
        this.rankFeelsSafeWalkingAloneD = rankFeelsSafeWalkingAloneD;
    }

    /**
     * (Required)
     *
     * @return The believeOtherPeopleCanBeTru
     */
    public Object getBelieveOtherPeopleCanBeTru() {
        return believeOtherPeopleCanBeTru;
    }

    /**
     * (Required)
     *
     * @param believeOtherPeopleCanBeTru The Believe_other_people_can_be_tru
     */
    public void setBelieveOtherPeopleCanBeTru(Object believeOtherPeopleCanBeTru) {
        this.believeOtherPeopleCanBeTru = believeOtherPeopleCanBeTru;
    }

    /**
     * (Required)
     *
     * @return The rankBelieveOtherPeopleCanB
     */
    public Object getRankBelieveOtherPeopleCanB() {
        return rankBelieveOtherPeopleCanB;
    }

    /**
     * (Required)
     *
     * @param rankBelieveOtherPeopleCanB The Rank_Believe_other_people_can_b
     */
    public void setRankBelieveOtherPeopleCanB(Object rankBelieveOtherPeopleCanB) {
        this.rankBelieveOtherPeopleCanB = rankBelieveOtherPeopleCanB;
    }

    /**
     * (Required)
     *
     * @return The spokeWithMoreThan5PeopleT
     */
    public Object getSpokeWithMoreThan5PeopleT() {
        return spokeWithMoreThan5PeopleT;
    }

    /**
     * (Required)
     *
     * @param spokeWithMoreThan5PeopleT The Spoke_with_more_than_5_people_t
     */
    public void setSpokeWithMoreThan5PeopleT(Object spokeWithMoreThan5PeopleT) {
        this.spokeWithMoreThan5PeopleT = spokeWithMoreThan5PeopleT;
    }

    /**
     * (Required)
     *
     * @return The rankSpokeWithMoreThan5Peo
     */
    public Object getRankSpokeWithMoreThan5Peo() {
        return rankSpokeWithMoreThan5Peo;
    }

    /**
     * (Required)
     *
     * @param rankSpokeWithMoreThan5Peo The Rank_Spoke_with_more_than_5_peo
     */
    public void setRankSpokeWithMoreThan5Peo(Object rankSpokeWithMoreThan5Peo) {
        this.rankSpokeWithMoreThan5Peo = rankSpokeWithMoreThan5Peo;
    }

    /**
     * (Required)
     *
     * @return The ableToDefinitelyGetHelpFro
     */
    public Object getAbleToDefinitelyGetHelpFro() {
        return ableToDefinitelyGetHelpFro;
    }

    /**
     * (Required)
     *
     * @param ableToDefinitelyGetHelpFro The Able_to_definitely_get_help_fro
     */
    public void setAbleToDefinitelyGetHelpFro(Object ableToDefinitelyGetHelpFro) {
        this.ableToDefinitelyGetHelpFro = ableToDefinitelyGetHelpFro;
    }

    /**
     * (Required)
     *
     * @return The rankAbleToDefinitelyGetHel
     */
    public Object getRankAbleToDefinitelyGetHel() {
        return rankAbleToDefinitelyGetHel;
    }

    /**
     * (Required)
     *
     * @param rankAbleToDefinitelyGetHel The Rank_Able_to_definitely_get_hel
     */
    public void setRankAbleToDefinitelyGetHel(Object rankAbleToDefinitelyGetHel) {
        this.rankAbleToDefinitelyGetHel = rankAbleToDefinitelyGetHel;
    }

    /**
     * (Required)
     *
     * @return The volunteers
     */
    public Object getVolunteers() {
        return volunteers;
    }

    /**
     * (Required)
     *
     * @param volunteers The Volunteers
     */
    public void setVolunteers(Object volunteers) {
        this.volunteers = volunteers;
    }

    /**
     * (Required)
     *
     * @return The rankVolunteers
     */
    public Object getRankVolunteers() {
        return rankVolunteers;
    }

    /**
     * (Required)
     *
     * @param rankVolunteers The Rank_Volunteers
     */
    public void setRankVolunteers(Object rankVolunteers) {
        this.rankVolunteers = rankVolunteers;
    }

    /**
     * (Required)
     *
     * @return The feelValuedBySociety
     */
    public Object getFeelValuedBySociety() {
        return feelValuedBySociety;
    }

    /**
     * (Required)
     *
     * @param feelValuedBySociety The Feel_valued_by_society
     */
    public void setFeelValuedBySociety(Object feelValuedBySociety) {
        this.feelValuedBySociety = feelValuedBySociety;
    }

    /**
     * (Required)
     *
     * @return The rankFeelValuedBySociety
     */
    public Object getRankFeelValuedBySociety() {
        return rankFeelValuedBySociety;
    }

    /**
     * (Required)
     *
     * @param rankFeelValuedBySociety The Rank_Feel_valued_by_society
     */
    public void setRankFeelValuedBySociety(Object rankFeelValuedBySociety) {
        this.rankFeelValuedBySociety = rankFeelValuedBySociety;
    }

    /**
     * (Required)
     *
     * @return The attendedALocalCommunityEven
     */
    public Object getAttendedALocalCommunityEven() {
        return attendedALocalCommunityEven;
    }

    /**
     * (Required)
     *
     * @param attendedALocalCommunityEven The Attended_a_local_community_even
     */
    public void setAttendedALocalCommunityEven(Object attendedALocalCommunityEven) {
        this.attendedALocalCommunityEven = attendedALocalCommunityEven;
    }

    /**
     * (Required)
     *
     * @return The rankAttendedALocalCommunity
     */
    public Object getRankAttendedALocalCommunity() {
        return rankAttendedALocalCommunity;
    }

    /**
     * (Required)
     *
     * @param rankAttendedALocalCommunity The Rank_Attended_a_local_community
     */
    public void setRankAttendedALocalCommunity(Object rankAttendedALocalCommunity) {
        this.rankAttendedALocalCommunity = rankAttendedALocalCommunity;
    }

    /**
     * (Required)
     *
     * @return The takeActionOnBehalfOfTheLo
     */
    public Object getTakeActionOnBehalfOfTheLo() {
        return takeActionOnBehalfOfTheLo;
    }

    /**
     * (Required)
     *
     * @param takeActionOnBehalfOfTheLo The Take_action_on_behalf_of_the_lo
     */
    public void setTakeActionOnBehalfOfTheLo(Object takeActionOnBehalfOfTheLo) {
        this.takeActionOnBehalfOfTheLo = takeActionOnBehalfOfTheLo;
    }

    /**
     * (Required)
     *
     * @return The rankTakeActionOnBehalfOfT
     */
    public Object getRankTakeActionOnBehalfOfT() {
        return rankTakeActionOnBehalfOfT;
    }

    /**
     * (Required)
     *
     * @param rankTakeActionOnBehalfOfT The Rank_Take_action_on_behalf_of_t
     */
    public void setRankTakeActionOnBehalfOfT(Object rankTakeActionOnBehalfOfT) {
        this.rankTakeActionOnBehalfOfT = rankTakeActionOnBehalfOfT;
    }

    /**
     * (Required)
     *
     * @return The membersOfASportsGroup
     */
    public Object getMembersOfASportsGroup() {
        return membersOfASportsGroup;
    }

    /**
     * (Required)
     *
     * @param membersOfASportsGroup The Members_of_a_sports_group
     */
    public void setMembersOfASportsGroup(Object membersOfASportsGroup) {
        this.membersOfASportsGroup = membersOfASportsGroup;
    }

    /**
     * (Required)
     *
     * @return The rankMembersOfASportsGroup
     */
    public Object getRankMembersOfASportsGroup() {
        return rankMembersOfASportsGroup;
    }

    /**
     * (Required)
     *
     * @param rankMembersOfASportsGroup The Rank_Members_of_a_sports_group
     */
    public void setRankMembersOfASportsGroup(Object rankMembersOfASportsGroup) {
        this.rankMembersOfASportsGroup = rankMembersOfASportsGroup;
    }

    /**
     * (Required)
     *
     * @return The membersOfAReligiousGroup
     */
    public Object getMembersOfAReligiousGroup() {
        return membersOfAReligiousGroup;
    }

    /**
     * (Required)
     *
     * @param membersOfAReligiousGroup The Members_of_a_religious_group
     */
    public void setMembersOfAReligiousGroup(Object membersOfAReligiousGroup) {
        this.membersOfAReligiousGroup = membersOfAReligiousGroup;
    }

    /**
     * (Required)
     *
     * @return The rankMembersOfAReligiousGro
     */
    public Object getRankMembersOfAReligiousGro() {
        return rankMembersOfAReligiousGro;
    }

    /**
     * (Required)
     *
     * @param rankMembersOfAReligiousGro The Rank_Members_of_a_religious_gro
     */
    public void setRankMembersOfAReligiousGro(Object rankMembersOfAReligiousGro) {
        this.rankMembersOfAReligiousGro = rankMembersOfAReligiousGro;
    }

    /**
     * (Required)
     *
     * @return The ratedTheirCommunityAsAnAct
     */
    public Object getRatedTheirCommunityAsAnAct() {
        return ratedTheirCommunityAsAnAct;
    }

    /**
     * (Required)
     *
     * @param ratedTheirCommunityAsAnAct The Rated_their_community_as_an_act
     */
    public void setRatedTheirCommunityAsAnAct(Object ratedTheirCommunityAsAnAct) {
        this.ratedTheirCommunityAsAnAct = ratedTheirCommunityAsAnAct;
    }

    /**
     * (Required)
     *
     * @return The rankRatedTheirCommunityAsA
     */
    public Object getRankRatedTheirCommunityAsA() {
        return rankRatedTheirCommunityAsA;
    }

    /**
     * (Required)
     *
     * @param rankRatedTheirCommunityAsA The Rank_Rated_their_community_as_a
     */
    public void setRankRatedTheirCommunityAsA(Object rankRatedTheirCommunityAsA) {
        this.rankRatedTheirCommunityAsA = rankRatedTheirCommunityAsA;
    }

    /**
     * (Required)
     *
     * @return The ratedTheirCommunityAsAPlea
     */
    public Object getRatedTheirCommunityAsAPlea() {
        return ratedTheirCommunityAsAPlea;
    }

    /**
     * (Required)
     *
     * @param ratedTheirCommunityAsAPlea The Rated_their_community_as_a_plea
     */
    public void setRatedTheirCommunityAsAPlea(Object ratedTheirCommunityAsAPlea) {
        this.ratedTheirCommunityAsAPlea = ratedTheirCommunityAsAPlea;
    }

    /**
     * (Required)
     *
     * @return The rankAtedTheirCommunityAsA
     */
    public Object getRankAtedTheirCommunityAsA() {
        return rankAtedTheirCommunityAsA;
    }

    /**
     * (Required)
     *
     * @param rankAtedTheirCommunityAsA The Rank_ated_their_community_as_a_
     */
    public void setRankAtedTheirCommunityAsA(Object rankAtedTheirCommunityAsA) {
        this.rankAtedTheirCommunityAsA = rankAtedTheirCommunityAsA;
    }

    /**
     * (Required)
     *
     * @return The ratedTheirCommunityAsGoodO
     */
    public Object getRatedTheirCommunityAsGoodO() {
        return ratedTheirCommunityAsGoodO;
    }

    /**
     * (Required)
     *
     * @param ratedTheirCommunityAsGoodO The Rated_their_community_as_good_o
     */
    public void setRatedTheirCommunityAsGoodO(Object ratedTheirCommunityAsGoodO) {
        this.ratedTheirCommunityAsGoodO = ratedTheirCommunityAsGoodO;
    }

    /**
     * (Required)
     *
     * @return The rankRatedTheirCommunityAsG
     */
    public Object getRankRatedTheirCommunityAsG() {
        return rankRatedTheirCommunityAsG;
    }

    /**
     * (Required)
     *
     * @param rankRatedTheirCommunityAsG The Rank_Rated_their_community_as_g
     */
    public void setRankRatedTheirCommunityAsG(Object rankRatedTheirCommunityAsG) {
        this.rankRatedTheirCommunityAsG = rankRatedTheirCommunityAsG;
    }

    /**
     * (Required)
     *
     * @return The indexOfRelativeSociaEconomi
     */
    public Object getIndexOfRelativeSociaEconomi() {
        return indexOfRelativeSociaEconomi;
    }

    /**
     * (Required)
     *
     * @param indexOfRelativeSociaEconomi The Index_of_Relative_Socia_Economi
     */
    public void setIndexOfRelativeSociaEconomi(Object indexOfRelativeSociaEconomi) {
        this.indexOfRelativeSociaEconomi = indexOfRelativeSociaEconomi;
    }

    /**
     * (Required)
     *
     * @return The rankIndexOfRelativeSociaEc
     */
    public Object getRankIndexOfRelativeSociaEc() {
        return rankIndexOfRelativeSociaEc;
    }

    /**
     * (Required)
     *
     * @param rankIndexOfRelativeSociaEc The Rank_Index_of_Relative_Socia_Ec
     */
    public void setRankIndexOfRelativeSociaEc(Object rankIndexOfRelativeSociaEc) {
        this.rankIndexOfRelativeSociaEc = rankIndexOfRelativeSociaEc;
    }

    /**
     * (Required)
     *
     * @return The unemploymentRate
     */
    public Object getUnemploymentRate() {
        return unemploymentRate;
    }

    /**
     * (Required)
     *
     * @param unemploymentRate The Unemployment_rate
     */
    public void setUnemploymentRate(Object unemploymentRate) {
        this.unemploymentRate = unemploymentRate;
    }

    /**
     * (Required)
     *
     * @return The rankUnemploymentRate
     */
    public Object getRankUnemploymentRate() {
        return rankUnemploymentRate;
    }

    /**
     * (Required)
     *
     * @param rankUnemploymentRate The Rank_Unemployment_rate
     */
    public void setRankUnemploymentRate(Object rankUnemploymentRate) {
        this.rankUnemploymentRate = rankUnemploymentRate;
    }

    /**
     * (Required)
     *
     * @return The percentIndividualIncomeLess
     */
    public Object getPercentIndividualIncomeLess() {
        return percentIndividualIncomeLess;
    }

    /**
     * (Required)
     *
     * @param percentIndividualIncomeLess The Percent_Individual_income_Less_
     */
    public void setPercentIndividualIncomeLess(Object percentIndividualIncomeLess) {
        this.percentIndividualIncomeLess = percentIndividualIncomeLess;
    }

    /**
     * (Required)
     *
     * @return The rankPercentIndividualIncome
     */
    public Object getRankPercentIndividualIncome() {
        return rankPercentIndividualIncome;
    }

    /**
     * (Required)
     *
     * @param rankPercentIndividualIncome The Rank_Percent_Individual_income_
     */
    public void setRankPercentIndividualIncome(Object rankPercentIndividualIncome) {
        this.rankPercentIndividualIncome = rankPercentIndividualIncome;
    }

    /**
     * (Required)
     *
     * @return The percentFemaleIncomeLessThan
     */
    public Object getPercentFemaleIncomeLessThan() {
        return percentFemaleIncomeLessThan;
    }

    /**
     * (Required)
     *
     * @param percentFemaleIncomeLessThan The Percent_Female_income_Less_than
     */
    public void setPercentFemaleIncomeLessThan(Object percentFemaleIncomeLessThan) {
        this.percentFemaleIncomeLessThan = percentFemaleIncomeLessThan;
    }

    /**
     * (Required)
     *
     * @return The rankPercentFemaleIncomeLess
     */
    public Object getRankPercentFemaleIncomeLess() {
        return rankPercentFemaleIncomeLess;
    }

    /**
     * (Required)
     *
     * @param rankPercentFemaleIncomeLess The Rank_Percent_Female_income_Less
     */
    public void setRankPercentFemaleIncomeLess(Object rankPercentFemaleIncomeLess) {
        this.rankPercentFemaleIncomeLess = rankPercentFemaleIncomeLess;
    }

    /**
     * (Required)
     *
     * @return The percentMaleIncomeLessThan4
     */
    public Object getPercentMaleIncomeLessThan4() {
        return percentMaleIncomeLessThan4;
    }

    /**
     * (Required)
     *
     * @param percentMaleIncomeLessThan4 The Percent_Male_income_Less_than_4
     */
    public void setPercentMaleIncomeLessThan4(Object percentMaleIncomeLessThan4) {
        this.percentMaleIncomeLessThan4 = percentMaleIncomeLessThan4;
    }

    /**
     * (Required)
     *
     * @return The rankPercentMaleIncomeLessT
     */
    public Object getRankPercentMaleIncomeLessT() {
        return rankPercentMaleIncomeLessT;
    }

    /**
     * (Required)
     *
     * @param rankPercentMaleIncomeLessT The Rank_Percent_Male_income_Less_t
     */
    public void setRankPercentMaleIncomeLessT(Object rankPercentMaleIncomeLessT) {
        this.rankPercentMaleIncomeLessT = rankPercentMaleIncomeLessT;
    }

    /**
     * (Required)
     *
     * @return The percentOneParentHeadedFamil
     */
    public Object getPercentOneParentHeadedFamil() {
        return percentOneParentHeadedFamil;
    }

    /**
     * (Required)
     *
     * @param percentOneParentHeadedFamil The Percent_One_Parent_headed_famil
     */
    public void setPercentOneParentHeadedFamil(Object percentOneParentHeadedFamil) {
        this.percentOneParentHeadedFamil = percentOneParentHeadedFamil;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOneParentHeaded
     */
    public Object getRankPercentOneParentHeaded() {
        return rankPercentOneParentHeaded;
    }

    /**
     * (Required)
     *
     * @param rankPercentOneParentHeaded The Rank_Percent_One_Parent_headed_
     */
    public void setRankPercentOneParentHeaded(Object rankPercentOneParentHeaded) {
        this.rankPercentOneParentHeaded = rankPercentOneParentHeaded;
    }

    /**
     * (Required)
     *
     * @return The oneParentHeadedFamiliesPerc
     */
    public Object getOneParentHeadedFamiliesPerc() {
        return oneParentHeadedFamiliesPerc;
    }

    /**
     * (Required)
     *
     * @param oneParentHeadedFamiliesPerc The One_Parent_headed_families_Perc
     */
    public void setOneParentHeadedFamiliesPerc(Object oneParentHeadedFamiliesPerc) {
        this.oneParentHeadedFamiliesPerc = oneParentHeadedFamiliesPerc;
    }

    /**
     * (Required)
     *
     * @return The rankOneParentHeadedFamilies
     */
    public Object getRankOneParentHeadedFamilies() {
        return rankOneParentHeadedFamilies;
    }

    /**
     * (Required)
     *
     * @param rankOneParentHeadedFamilies The Rank_One_Parent_headed_families
     */
    public void setRankOneParentHeadedFamilies(Object rankOneParentHeadedFamilies) {
        this.rankOneParentHeadedFamilies = rankOneParentHeadedFamilies;
    }

    /**
     * (Required)
     *
     * @return The oneParentHeadedFamiliesPe1
     */
    public Object getOneParentHeadedFamiliesPe1() {
        return oneParentHeadedFamiliesPe1;
    }

    /**
     * (Required)
     *
     * @param oneParentHeadedFamiliesPe1 The One_Parent_headed_families_Pe_1
     */
    public void setOneParentHeadedFamiliesPe1(Object oneParentHeadedFamiliesPe1) {
        this.oneParentHeadedFamiliesPe1 = oneParentHeadedFamiliesPe1;
    }

    /**
     * (Required)
     *
     * @return The rankOneParentHeadedFamili1
     */
    public Object getRankOneParentHeadedFamili1() {
        return rankOneParentHeadedFamili1;
    }

    /**
     * (Required)
     *
     * @param rankOneParentHeadedFamili1 The Rank_One_Parent_headed_famili_1
     */
    public void setRankOneParentHeadedFamili1(Object rankOneParentHeadedFamili1) {
        this.rankOneParentHeadedFamili1 = rankOneParentHeadedFamili1;
    }

    /**
     * (Required)
     *
     * @return The equivalisedMedianIncome
     */
    public Object getEquivalisedMedianIncome() {
        return equivalisedMedianIncome;
    }

    /**
     * (Required)
     *
     * @param equivalisedMedianIncome The Equivalised_median_income
     */
    public void setEquivalisedMedianIncome(Object equivalisedMedianIncome) {
        this.equivalisedMedianIncome = equivalisedMedianIncome;
    }

    /**
     * (Required)
     *
     * @return The rankEquivalisedMedianIncome
     */
    public Object getRankEquivalisedMedianIncome() {
        return rankEquivalisedMedianIncome;
    }

    /**
     * (Required)
     *
     * @param rankEquivalisedMedianIncome The Rank_Equivalised_median_income
     */
    public void setRankEquivalisedMedianIncome(Object rankEquivalisedMedianIncome) {
        this.rankEquivalisedMedianIncome = rankEquivalisedMedianIncome;
    }

    /**
     * (Required)
     *
     * @return The delayedMedicalConsultationBe
     */
    public Object getDelayedMedicalConsultationBe() {
        return delayedMedicalConsultationBe;
    }

    /**
     * (Required)
     *
     * @param delayedMedicalConsultationBe The Delayed_medical_consultation_be
     */
    public void setDelayedMedicalConsultationBe(Object delayedMedicalConsultationBe) {
        this.delayedMedicalConsultationBe = delayedMedicalConsultationBe;
    }

    /**
     * (Required)
     *
     * @return The rankDelayedMedicalConsultati
     */
    public Object getRankDelayedMedicalConsultati() {
        return rankDelayedMedicalConsultati;
    }

    /**
     * (Required)
     *
     * @param rankDelayedMedicalConsultati The Rank_Delayed_medical_consultati
     */
    public void setRankDelayedMedicalConsultati(Object rankDelayedMedicalConsultati) {
        this.rankDelayedMedicalConsultati = rankDelayedMedicalConsultati;
    }

    /**
     * (Required)
     *
     * @return The delayedPurchasingPrescribedM
     */
    public Object getDelayedPurchasingPrescribedM() {
        return delayedPurchasingPrescribedM;
    }

    /**
     * (Required)
     *
     * @param delayedPurchasingPrescribedM The Delayed_purchasing_prescribed_m
     */
    public void setDelayedPurchasingPrescribedM(Object delayedPurchasingPrescribedM) {
        this.delayedPurchasingPrescribedM = delayedPurchasingPrescribedM;
    }

    /**
     * (Required)
     *
     * @return The rankDelayedPurchasingPrescri
     */
    public Object getRankDelayedPurchasingPrescri() {
        return rankDelayedPurchasingPrescri;
    }

    /**
     * (Required)
     *
     * @param rankDelayedPurchasingPrescri The Rank_Delayed_purchasing_prescri
     */
    public void setRankDelayedPurchasingPrescri(Object rankDelayedPurchasingPrescri) {
        this.rankDelayedPurchasingPrescri = rankDelayedPurchasingPrescri;
    }

    /**
     * (Required)
     *
     * @return The percentLowIncomeWelfareDepe
     */
    public Object getPercentLowIncomeWelfareDepe() {
        return percentLowIncomeWelfareDepe;
    }

    /**
     * (Required)
     *
     * @param percentLowIncomeWelfareDepe The Percent_low_income_welfare_depe
     */
    public void setPercentLowIncomeWelfareDepe(Object percentLowIncomeWelfareDepe) {
        this.percentLowIncomeWelfareDepe = percentLowIncomeWelfareDepe;
    }

    /**
     * (Required)
     *
     * @return The rankPercentLowIncomeWelfare
     */
    public Object getRankPercentLowIncomeWelfare() {
        return rankPercentLowIncomeWelfare;
    }

    /**
     * (Required)
     *
     * @param rankPercentLowIncomeWelfare The Rank_Percent_low_income_welfare
     */
    public void setRankPercentLowIncomeWelfare(Object rankPercentLowIncomeWelfare) {
        this.rankPercentLowIncomeWelfare = rankPercentLowIncomeWelfare;
    }

    /**
     * (Required)
     *
     * @return The percentOfPopWithFoodInsecu
     */
    public Object getPercentOfPopWithFoodInsecu() {
        return percentOfPopWithFoodInsecu;
    }

    /**
     * (Required)
     *
     * @param percentOfPopWithFoodInsecu The Percent_of_pop_with_food_insecu
     */
    public void setPercentOfPopWithFoodInsecu(Object percentOfPopWithFoodInsecu) {
        this.percentOfPopWithFoodInsecu = percentOfPopWithFoodInsecu;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPopWithFoodI
     */
    public Object getRankPercentOfPopWithFoodI() {
        return rankPercentOfPopWithFoodI;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPopWithFoodI The Rank_Percent_of_pop_with_food_i
     */
    public void setRankPercentOfPopWithFoodI(Object rankPercentOfPopWithFoodI) {
        this.rankPercentOfPopWithFoodI = rankPercentOfPopWithFoodI;
    }

    /**
     * (Required)
     *
     * @return The percentMortgageStress
     */
    public Object getPercentMortgageStress() {
        return percentMortgageStress;
    }

    /**
     * (Required)
     *
     * @param percentMortgageStress The Percent_Mortgage_Stress
     */
    public void setPercentMortgageStress(Object percentMortgageStress) {
        this.percentMortgageStress = percentMortgageStress;
    }

    /**
     * (Required)
     *
     * @return The rankPercentMortgageStress
     */
    public Object getRankPercentMortgageStress() {
        return rankPercentMortgageStress;
    }

    /**
     * (Required)
     *
     * @param rankPercentMortgageStress The Rank_Percent_Mortgage_Stress
     */
    public void setRankPercentMortgageStress(Object rankPercentMortgageStress) {
        this.rankPercentMortgageStress = rankPercentMortgageStress;
    }

    /**
     * (Required)
     *
     * @return The percentRentalStress
     */
    public Object getPercentRentalStress() {
        return percentRentalStress;
    }

    /**
     * (Required)
     *
     * @param percentRentalStress The Percent_Rental_Stress
     */
    public void setPercentRentalStress(Object percentRentalStress) {
        this.percentRentalStress = percentRentalStress;
    }

    /**
     * (Required)
     *
     * @return The rankPercentRentalStress
     */
    public Object getRankPercentRentalStress() {
        return rankPercentRentalStress;
    }

    /**
     * (Required)
     *
     * @param rankPercentRentalStress The Rank_Percent_Rental_Stress
     */
    public void setRankPercentRentalStress(Object rankPercentRentalStress) {
        this.rankPercentRentalStress = rankPercentRentalStress;
    }

    /**
     * (Required)
     *
     * @return The percentOfRentalHousingThat
     */
    public Object getPercentOfRentalHousingThat() {
        return percentOfRentalHousingThat;
    }

    /**
     * (Required)
     *
     * @param percentOfRentalHousingThat The Percent_of_rental_housing_that_
     */
    public void setPercentOfRentalHousingThat(Object percentOfRentalHousingThat) {
        this.percentOfRentalHousingThat = percentOfRentalHousingThat;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfRentalHousing
     */
    public Object getRankPercentOfRentalHousing() {
        return rankPercentOfRentalHousing;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfRentalHousing The Rank_Percent_of_rental_housing_
     */
    public void setRankPercentOfRentalHousing(Object rankPercentOfRentalHousing) {
        this.rankPercentOfRentalHousing = rankPercentOfRentalHousing;
    }

    /**
     * (Required)
     *
     * @return The medianHousePrice
     */
    public Object getMedianHousePrice() {
        return medianHousePrice;
    }

    /**
     * (Required)
     *
     * @param medianHousePrice The Median_house_price
     */
    public void setMedianHousePrice(Object medianHousePrice) {
        this.medianHousePrice = medianHousePrice;
    }

    /**
     * (Required)
     *
     * @return The rankMedianHousePrice
     */
    public Object getRankMedianHousePrice() {
        return rankMedianHousePrice;
    }

    /**
     * (Required)
     *
     * @param rankMedianHousePrice The Rank_Median_house_price
     */
    public void setRankMedianHousePrice(Object rankMedianHousePrice) {
        this.rankMedianHousePrice = rankMedianHousePrice;
    }

    /**
     * (Required)
     *
     * @return The medianRentFor3BedroomsHome
     */
    public Object getMedianRentFor3BedroomsHome() {
        return medianRentFor3BedroomsHome;
    }

    /**
     * (Required)
     *
     * @param medianRentFor3BedroomsHome The Median_rent_for_3_bedrooms_home
     */
    public void setMedianRentFor3BedroomsHome(Object medianRentFor3BedroomsHome) {
        this.medianRentFor3BedroomsHome = medianRentFor3BedroomsHome;
    }

    /**
     * (Required)
     *
     * @return The rankMedianRentFor3Bedrooms
     */
    public Object getRankMedianRentFor3Bedrooms() {
        return rankMedianRentFor3Bedrooms;
    }

    /**
     * (Required)
     *
     * @param rankMedianRentFor3Bedrooms The Rank_Median_rent_for_3_bedrooms
     */
    public void setRankMedianRentFor3Bedrooms(Object rankMedianRentFor3Bedrooms) {
        this.rankMedianRentFor3Bedrooms = rankMedianRentFor3Bedrooms;
    }

    /**
     * (Required)
     *
     * @return The newDwellingsApprovedForCons
     */
    public Object getNewDwellingsApprovedForCons() {
        return newDwellingsApprovedForCons;
    }

    /**
     * (Required)
     *
     * @param newDwellingsApprovedForCons The New_dwellings_approved_for_cons
     */
    public void setNewDwellingsApprovedForCons(Object newDwellingsApprovedForCons) {
        this.newDwellingsApprovedForCons = newDwellingsApprovedForCons;
    }

    /**
     * (Required)
     *
     * @return The rankNewDwellingsApprovedFor
     */
    public Object getRankNewDwellingsApprovedFor() {
        return rankNewDwellingsApprovedFor;
    }

    /**
     * (Required)
     *
     * @param rankNewDwellingsApprovedFor The Rank_New_dwellings_approved_for
     */
    public void setRankNewDwellingsApprovedFor(Object rankNewDwellingsApprovedFor) {
        this.rankNewDwellingsApprovedFor = rankNewDwellingsApprovedFor;
    }

    /**
     * (Required)
     *
     * @return The socialHousingStockAsAPerce
     */
    public Object getSocialHousingStockAsAPerce() {
        return socialHousingStockAsAPerce;
    }

    /**
     * (Required)
     *
     * @param socialHousingStockAsAPerce The Social_housing_stock_as_a_Perce
     */
    public void setSocialHousingStockAsAPerce(Object socialHousingStockAsAPerce) {
        this.socialHousingStockAsAPerce = socialHousingStockAsAPerce;
    }

    /**
     * (Required)
     *
     * @return The rankSocialHousingStockAsA
     */
    public Object getRankSocialHousingStockAsA() {
        return rankSocialHousingStockAsA;
    }

    /**
     * (Required)
     *
     * @param rankSocialHousingStockAsA The Rank_Social_housing_stock_as_a_
     */
    public void setRankSocialHousingStockAsA(Object rankSocialHousingStockAsA) {
        this.rankSocialHousingStockAsA = rankSocialHousingStockAsA;
    }

    /**
     * (Required)
     *
     * @return The numberOfSocialHousingDwelli
     */
    public Object getNumberOfSocialHousingDwelli() {
        return numberOfSocialHousingDwelli;
    }

    /**
     * (Required)
     *
     * @param numberOfSocialHousingDwelli The Number_of_Social_housing_dwelli
     */
    public void setNumberOfSocialHousingDwelli(Object numberOfSocialHousingDwelli) {
        this.numberOfSocialHousingDwelli = numberOfSocialHousingDwelli;
    }

    /**
     * (Required)
     *
     * @return The rankNumberOfSocialHousingD
     */
    public Object getRankNumberOfSocialHousingD() {
        return rankNumberOfSocialHousingD;
    }

    /**
     * (Required)
     *
     * @param rankNumberOfSocialHousingD The Rank_Number_of_Social_housing_d
     */
    public void setRankNumberOfSocialHousingD(Object rankNumberOfSocialHousingD) {
        this.rankNumberOfSocialHousingD = rankNumberOfSocialHousingD;
    }

    /**
     * (Required)
     *
     * @return The homelessnessRatePer1000Pop
     */
    public Object getHomelessnessRatePer1000Pop() {
        return homelessnessRatePer1000Pop;
    }

    /**
     * (Required)
     *
     * @param homelessnessRatePer1000Pop The Homelessness_rate_per_1000_pop
     */
    public void setHomelessnessRatePer1000Pop(Object homelessnessRatePer1000Pop) {
        this.homelessnessRatePer1000Pop = homelessnessRatePer1000Pop;
    }

    /**
     * (Required)
     *
     * @return The rankHomelessnessRatePer1000
     */
    public Object getRankHomelessnessRatePer1000() {
        return rankHomelessnessRatePer1000;
    }

    /**
     * (Required)
     *
     * @param rankHomelessnessRatePer1000 The Rank_Homelessness_rate_per_1000
     */
    public void setRankHomelessnessRatePer1000(Object rankHomelessnessRatePer1000) {
        this.rankHomelessnessRatePer1000 = rankHomelessnessRatePer1000;
    }

    /**
     * (Required)
     *
     * @return The percentOfWorkJourneysWhich
     */
    public Object getPercentOfWorkJourneysWhich() {
        return percentOfWorkJourneysWhich;
    }

    /**
     * (Required)
     *
     * @param percentOfWorkJourneysWhich The Percent_of_work_journeys_which_
     */
    public void setPercentOfWorkJourneysWhich(Object percentOfWorkJourneysWhich) {
        this.percentOfWorkJourneysWhich = percentOfWorkJourneysWhich;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfWorkJourneysW
     */
    public Object getRankPercentOfWorkJourneysW() {
        return rankPercentOfWorkJourneysW;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfWorkJourneysW The Rank_Percent_of_work_journeys_w
     */
    public void setRankPercentOfWorkJourneysW(Object rankPercentOfWorkJourneysW) {
        this.rankPercentOfWorkJourneysW = rankPercentOfWorkJourneysW;
    }

    /**
     * (Required)
     *
     * @return The percentOfWorkJourneysWhich1
     */
    public Object getPercentOfWorkJourneysWhich1() {
        return percentOfWorkJourneysWhich1;
    }

    /**
     * (Required)
     *
     * @param percentOfWorkJourneysWhich1 The Percent_of_work_journeys_which1
     */
    public void setPercentOfWorkJourneysWhich1(Object percentOfWorkJourneysWhich1) {
        this.percentOfWorkJourneysWhich1 = percentOfWorkJourneysWhich1;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfWorkJourneys1
     */
    public Object getRankPercentOfWorkJourneys1() {
        return rankPercentOfWorkJourneys1;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfWorkJourneys1 The Rank_Percent_of_work_journeys_1
     */
    public void setRankPercentOfWorkJourneys1(Object rankPercentOfWorkJourneys1) {
        this.rankPercentOfWorkJourneys1 = rankPercentOfWorkJourneys1;
    }

    /**
     * (Required)
     *
     * @return The personsWithAtLeast2HourDa
     */
    public Object getPersonsWithAtLeast2HourDa() {
        return personsWithAtLeast2HourDa;
    }

    /**
     * (Required)
     *
     * @param personsWithAtLeast2HourDa The Persons_with_at_least_2_hour_da
     */
    public void setPersonsWithAtLeast2HourDa(Object personsWithAtLeast2HourDa) {
        this.personsWithAtLeast2HourDa = personsWithAtLeast2HourDa;
    }

    /**
     * (Required)
     *
     * @return The rankPersonsWithAtLeast2Ho
     */
    public Object getRankPersonsWithAtLeast2Ho() {
        return rankPersonsWithAtLeast2Ho;
    }

    /**
     * (Required)
     *
     * @param rankPersonsWithAtLeast2Ho The Rank_Persons_with_at_least_2_ho
     */
    public void setRankPersonsWithAtLeast2Ho(Object rankPersonsWithAtLeast2Ho) {
        this.rankPersonsWithAtLeast2Ho = rankPersonsWithAtLeast2Ho;
    }

    /**
     * (Required)
     *
     * @return The percentHouseholdsWithNoMoto
     */
    public Object getPercentHouseholdsWithNoMoto() {
        return percentHouseholdsWithNoMoto;
    }

    /**
     * (Required)
     *
     * @param percentHouseholdsWithNoMoto The Percent_households_with_no_moto
     */
    public void setPercentHouseholdsWithNoMoto(Object percentHouseholdsWithNoMoto) {
        this.percentHouseholdsWithNoMoto = percentHouseholdsWithNoMoto;
    }

    /**
     * (Required)
     *
     * @return The rankPercentHouseholdsWithNo
     */
    public Object getRankPercentHouseholdsWithNo() {
        return rankPercentHouseholdsWithNo;
    }

    /**
     * (Required)
     *
     * @param rankPercentHouseholdsWithNo The Rank_Percent_households_with_no
     */
    public void setRankPercentHouseholdsWithNo(Object rankPercentHouseholdsWithNo) {
        this.rankPercentHouseholdsWithNo = rankPercentHouseholdsWithNo;
    }

    /**
     * (Required)
     *
     * @return The fTEStudents
     */
    public Object getFTEStudents() {
        return fTEStudents;
    }

    /**
     * (Required)
     *
     * @param fTEStudents The FTE_Students
     */
    public void setFTEStudents(Object fTEStudents) {
        this.fTEStudents = fTEStudents;
    }

    /**
     * (Required)
     *
     * @return The percentYear9StudentsWhoAtt
     */
    public Object getPercentYear9StudentsWhoAtt() {
        return percentYear9StudentsWhoAtt;
    }

    /**
     * (Required)
     *
     * @param percentYear9StudentsWhoAtt The Percent_year_9_students_who_att
     */
    public void setPercentYear9StudentsWhoAtt(Object percentYear9StudentsWhoAtt) {
        this.percentYear9StudentsWhoAtt = percentYear9StudentsWhoAtt;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfYear9Students
     */
    public Object getRankPercentOfYear9Students() {
        return rankPercentOfYear9Students;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfYear9Students The Rank_Percent_of_year_9_students
     */
    public void setRankPercentOfYear9Students(Object rankPercentOfYear9Students) {
        this.rankPercentOfYear9Students = rankPercentOfYear9Students;
    }

    /**
     * (Required)
     *
     * @return The percentOfYear9StudentsWho
     */
    public Object getPercentOfYear9StudentsWho() {
        return percentOfYear9StudentsWho;
    }

    /**
     * (Required)
     *
     * @param percentOfYear9StudentsWho The Percent_of_year_9_students_who_
     */
    public void setPercentOfYear9StudentsWho(Object percentOfYear9StudentsWho) {
        this.percentOfYear9StudentsWho = percentOfYear9StudentsWho;
    }

    /**
     * (Required)
     *
     * @return The rankPercentYear9StudentsWh
     */
    public Object getRankPercentYear9StudentsWh() {
        return rankPercentYear9StudentsWh;
    }

    /**
     * (Required)
     *
     * @param rankPercentYear9StudentsWh The Rank_Percent_year_9_students_wh
     */
    public void setRankPercentYear9StudentsWh(Object rankPercentYear9StudentsWh) {
        this.rankPercentYear9StudentsWh = rankPercentYear9StudentsWh;
    }

    /**
     * (Required)
     *
     * @return The percent19YearOldsCompleting
     */
    public Object getPercent19YearOldsCompleting() {
        return percent19YearOldsCompleting;
    }

    /**
     * (Required)
     *
     * @param percent19YearOldsCompleting The Percent_19_year_olds_completing
     */
    public void setPercent19YearOldsCompleting(Object percent19YearOldsCompleting) {
        this.percent19YearOldsCompleting = percent19YearOldsCompleting;
    }

    /**
     * (Required)
     *
     * @return The rankPercent19YearOldsCompl
     */
    public Object getRankPercent19YearOldsCompl() {
        return rankPercent19YearOldsCompl;
    }

    /**
     * (Required)
     *
     * @param rankPercent19YearOldsCompl The Rank_Percent_19_year_olds_compl
     */
    public void setRankPercent19YearOldsCompl(Object rankPercent19YearOldsCompl) {
        this.rankPercent19YearOldsCompl = rankPercent19YearOldsCompl;
    }

    /**
     * (Required)
     *
     * @return The percentPersonsWhoDidNotCom
     */
    public Object getPercentPersonsWhoDidNotCom() {
        return percentPersonsWhoDidNotCom;
    }

    /**
     * (Required)
     *
     * @param percentPersonsWhoDidNotCom The Percent_persons_who_did_not_com
     */
    public void setPercentPersonsWhoDidNotCom(Object percentPersonsWhoDidNotCom) {
        this.percentPersonsWhoDidNotCom = percentPersonsWhoDidNotCom;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsWhoDid
     */
    public Object getRankPercentOfPersonsWhoDid() {
        return rankPercentOfPersonsWhoDid;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsWhoDid The Rank_Percent_of_persons_who_did
     */
    public void setRankPercentOfPersonsWhoDid(Object rankPercentOfPersonsWhoDid) {
        this.rankPercentOfPersonsWhoDid = rankPercentOfPersonsWhoDid;
    }

    /**
     * (Required)
     *
     * @return The percentPersonsWhoCompletedA
     */
    public Object getPercentPersonsWhoCompletedA() {
        return percentPersonsWhoCompletedA;
    }

    /**
     * (Required)
     *
     * @param percentPersonsWhoCompletedA The Percent_persons_who_completed_a
     */
    public void setPercentPersonsWhoCompletedA(Object percentPersonsWhoCompletedA) {
        this.percentPersonsWhoCompletedA = percentPersonsWhoCompletedA;
    }

    /**
     * (Required)
     *
     * @return The rankPercentPersonsWhoComple
     */
    public Object getRankPercentPersonsWhoComple() {
        return rankPercentPersonsWhoComple;
    }

    /**
     * (Required)
     *
     * @param rankPercentPersonsWhoComple The Rank_Percent_persons_who_comple
     */
    public void setRankPercentPersonsWhoComple(Object rankPercentPersonsWhoComple) {
        this.rankPercentPersonsWhoComple = rankPercentPersonsWhoComple;
    }

    /**
     * (Required)
     *
     * @return The percentOfSchoolChildrenAtte
     */
    public Object getPercentOfSchoolChildrenAtte() {
        return percentOfSchoolChildrenAtte;
    }

    /**
     * (Required)
     *
     * @param percentOfSchoolChildrenAtte The Percent_of_school_children_atte
     */
    public void setPercentOfSchoolChildrenAtte(Object percentOfSchoolChildrenAtte) {
        this.percentOfSchoolChildrenAtte = percentOfSchoolChildrenAtte;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfSchoolChildren
     */
    public Object getRankPercentOfSchoolChildren() {
        return rankPercentOfSchoolChildren;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfSchoolChildren The Rank_Percent_of_school_children
     */
    public void setRankPercentOfSchoolChildren(Object rankPercentOfSchoolChildren) {
        this.rankPercentOfSchoolChildren = rankPercentOfSchoolChildren;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsReportingAs
     */
    public Object getPercentOfPersonsReportingAs() {
        return percentOfPersonsReportingAs;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsReportingAs The Percent_of_persons_reporting_as
     */
    public void setPercentOfPersonsReportingAs(Object percentOfPersonsReportingAs) {
        this.percentOfPersonsReportingAs = percentOfPersonsReportingAs;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsReporti
     */
    public Object getRankPercentOfPersonsReporti() {
        return rankPercentOfPersonsReporti;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsReporti The Rank_Percent_of_persons_reporti
     */
    public void setRankPercentOfPersonsReporti(Object rankPercentOfPersonsReporti) {
        this.rankPercentOfPersonsReporti = rankPercentOfPersonsReporti;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsReportingTy
     */
    public Object getPercentOfPersonsReportingTy() {
        return percentOfPersonsReportingTy;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsReportingTy The Percent_of_persons_reporting_ty
     */
    public void setPercentOfPersonsReportingTy(Object percentOfPersonsReportingTy) {
        this.percentOfPersonsReportingTy = percentOfPersonsReportingTy;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsRepor1
     */
    public Object getRankPercentOfPersonsRepor1() {
        return rankPercentOfPersonsRepor1;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsRepor1 The Rank_Percent_of_persons_repor_1
     */
    public void setRankPercentOfPersonsRepor1(Object rankPercentOfPersonsRepor1) {
        this.rankPercentOfPersonsRepor1 = rankPercentOfPersonsRepor1;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsReportingHi
     */
    public Object getPercentOfPersonsReportingHi() {
        return percentOfPersonsReportingHi;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsReportingHi The Percent_of_persons_reporting_hi
     */
    public void setPercentOfPersonsReportingHi(Object percentOfPersonsReportingHi) {
        this.percentOfPersonsReportingHi = percentOfPersonsReportingHi;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsRepor2
     */
    public Object getRankPercentOfPersonsRepor2() {
        return rankPercentOfPersonsRepor2;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsRepor2 The Rank_Percent_of_persons_repor_2
     */
    public void setRankPercentOfPersonsRepor2(Object rankPercentOfPersonsRepor2) {
        this.rankPercentOfPersonsRepor2 = rankPercentOfPersonsRepor2;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsReportingHe
     */
    public Object getPercentOfPersonsReportingHe() {
        return percentOfPersonsReportingHe;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsReportingHe The Percent_of_persons_reporting_he
     */
    public void setPercentOfPersonsReportingHe(Object percentOfPersonsReportingHe) {
        this.percentOfPersonsReportingHe = percentOfPersonsReportingHe;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsRepor3
     */
    public Object getRankPercentOfPersonsRepor3() {
        return rankPercentOfPersonsRepor3;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsRepor3 The Rank_Percent_of_persons_repor_3
     */
    public void setRankPercentOfPersonsRepor3(Object rankPercentOfPersonsRepor3) {
        this.rankPercentOfPersonsRepor3 = rankPercentOfPersonsRepor3;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsReportingOs
     */
    public Object getPercentOfPersonsReportingOs() {
        return percentOfPersonsReportingOs;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsReportingOs The Percent_of_persons_reporting_os
     */
    public void setPercentOfPersonsReportingOs(Object percentOfPersonsReportingOs) {
        this.percentOfPersonsReportingOs = percentOfPersonsReportingOs;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsRepor4
     */
    public Object getRankPercentOfPersonsRepor4() {
        return rankPercentOfPersonsRepor4;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsRepor4 The Rank_Percent_of_persons_repor_4
     */
    public void setRankPercentOfPersonsRepor4(Object rankPercentOfPersonsRepor4) {
        this.rankPercentOfPersonsRepor4 = rankPercentOfPersonsRepor4;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsReportingAr
     */
    public Object getPercentOfPersonsReportingAr() {
        return percentOfPersonsReportingAr;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsReportingAr The Percent_of_persons_reporting_ar
     */
    public void setPercentOfPersonsReportingAr(Object percentOfPersonsReportingAr) {
        this.percentOfPersonsReportingAr = percentOfPersonsReportingAr;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsRepor5
     */
    public Object getRankPercentOfPersonsRepor5() {
        return rankPercentOfPersonsRepor5;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsRepor5 The Rank_Percent_of_persons_repor_5
     */
    public void setRankPercentOfPersonsRepor5(Object rankPercentOfPersonsRepor5) {
        this.rankPercentOfPersonsRepor5 = rankPercentOfPersonsRepor5;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsWhoAreOver
     */
    public Object getPercentOfPersonsWhoAreOver() {
        return percentOfPersonsWhoAreOver;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsWhoAreOver The Percent_of_persons_who_are_over
     */
    public void setPercentOfPersonsWhoAreOver(Object percentOfPersonsWhoAreOver) {
        this.percentOfPersonsWhoAreOver = percentOfPersonsWhoAreOver;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsWhoAre
     */
    public Object getRankPercentOfPersonsWhoAre() {
        return rankPercentOfPersonsWhoAre;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsWhoAre The Rank_Percent_of_persons_who_are
     */
    public void setRankPercentOfPersonsWhoAre(Object rankPercentOfPersonsWhoAre) {
        this.rankPercentOfPersonsWhoAre = rankPercentOfPersonsWhoAre;
    }

    /**
     * (Required)
     *
     * @return The percentOfFemalesWhoAreOver
     */
    public Object getPercentOfFemalesWhoAreOver() {
        return percentOfFemalesWhoAreOver;
    }

    /**
     * (Required)
     *
     * @param percentOfFemalesWhoAreOver The Percent_of_females_who_are_over
     */
    public void setPercentOfFemalesWhoAreOver(Object percentOfFemalesWhoAreOver) {
        this.percentOfFemalesWhoAreOver = percentOfFemalesWhoAreOver;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfFemalesWhoAre
     */
    public Object getRankPercentOfFemalesWhoAre() {
        return rankPercentOfFemalesWhoAre;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfFemalesWhoAre The Rank_Percent_of_females_who_are
     */
    public void setRankPercentOfFemalesWhoAre(Object rankPercentOfFemalesWhoAre) {
        this.rankPercentOfFemalesWhoAre = rankPercentOfFemalesWhoAre;
    }

    /**
     * (Required)
     *
     * @return The percentOfMalesWhoAreOverwe
     */
    public Object getPercentOfMalesWhoAreOverwe() {
        return percentOfMalesWhoAreOverwe;
    }

    /**
     * (Required)
     *
     * @param percentOfMalesWhoAreOverwe The Percent_of_males_who_are_overwe
     */
    public void setPercentOfMalesWhoAreOverwe(Object percentOfMalesWhoAreOverwe) {
        this.percentOfMalesWhoAreOverwe = percentOfMalesWhoAreOverwe;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfMalesWhoAreO
     */
    public Object getRankPercentOfMalesWhoAreO() {
        return rankPercentOfMalesWhoAreO;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfMalesWhoAreO The Rank_Percent_of_males_who_are_o
     */
    public void setRankPercentOfMalesWhoAreO(Object rankPercentOfMalesWhoAreO) {
        this.rankPercentOfMalesWhoAreO = rankPercentOfMalesWhoAreO;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsWhoAreObes
     */
    public Object getPercentOfPersonsWhoAreObes() {
        return percentOfPersonsWhoAreObes;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsWhoAreObes The Percent_of_persons_who_are_obes
     */
    public void setPercentOfPersonsWhoAreObes(Object percentOfPersonsWhoAreObes) {
        this.percentOfPersonsWhoAreObes = percentOfPersonsWhoAreObes;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsWhoA1
     */
    public Object getRankPercentOfPersonsWhoA1() {
        return rankPercentOfPersonsWhoA1;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsWhoA1 The Rank_Percent_of_persons_who_a_1
     */
    public void setRankPercentOfPersonsWhoA1(Object rankPercentOfPersonsWhoA1) {
        this.rankPercentOfPersonsWhoA1 = rankPercentOfPersonsWhoA1;
    }

    /**
     * (Required)
     *
     * @return The percentOfFemalesWhoAreObes
     */
    public Object getPercentOfFemalesWhoAreObes() {
        return percentOfFemalesWhoAreObes;
    }

    /**
     * (Required)
     *
     * @param percentOfFemalesWhoAreObes The Percent_of_females_who_are_obes
     */
    public void setPercentOfFemalesWhoAreObes(Object percentOfFemalesWhoAreObes) {
        this.percentOfFemalesWhoAreObes = percentOfFemalesWhoAreObes;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfFemalesWhoA1
     */
    public Object getRankPercentOfFemalesWhoA1() {
        return rankPercentOfFemalesWhoA1;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfFemalesWhoA1 The Rank_Percent_of_females_who_a_1
     */
    public void setRankPercentOfFemalesWhoA1(Object rankPercentOfFemalesWhoA1) {
        this.rankPercentOfFemalesWhoA1 = rankPercentOfFemalesWhoA1;
    }

    /**
     * (Required)
     *
     * @return The percentOfMalesWhoAreObese
     */
    public Object getPercentOfMalesWhoAreObese() {
        return percentOfMalesWhoAreObese;
    }

    /**
     * (Required)
     *
     * @param percentOfMalesWhoAreObese The Percent_of_males_who_are_obese
     */
    public void setPercentOfMalesWhoAreObese(Object percentOfMalesWhoAreObese) {
        this.percentOfMalesWhoAreObese = percentOfMalesWhoAreObese;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfMalesWhoAre1
     */
    public Object getRankPercentOfMalesWhoAre1() {
        return rankPercentOfMalesWhoAre1;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfMalesWhoAre1 The Rank_Percent_of_males_who_are_1
     */
    public void setRankPercentOfMalesWhoAre1(Object rankPercentOfMalesWhoAre1) {
        this.rankPercentOfMalesWhoAre1 = rankPercentOfMalesWhoAre1;
    }

    /**
     * (Required)
     *
     * @return The malignantCancersDiagnosedPer
     */
    public Object getMalignantCancersDiagnosedPer() {
        return malignantCancersDiagnosedPer;
    }

    /**
     * (Required)
     *
     * @param malignantCancersDiagnosedPer The Malignant_cancers_diagnosed_per
     */
    public void setMalignantCancersDiagnosedPer(Object malignantCancersDiagnosedPer) {
        this.malignantCancersDiagnosedPer = malignantCancersDiagnosedPer;
    }

    /**
     * (Required)
     *
     * @return The rankMalignantCancersDiagnose
     */
    public Object getRankMalignantCancersDiagnose() {
        return rankMalignantCancersDiagnose;
    }

    /**
     * (Required)
     *
     * @param rankMalignantCancersDiagnose The Rank_Malignant_cancers_diagnose
     */
    public void setRankMalignantCancersDiagnose(Object rankMalignantCancersDiagnose) {
        this.rankMalignantCancersDiagnose = rankMalignantCancersDiagnose;
    }

    /**
     * (Required)
     *
     * @return The maleCancerIncidencePer1000
     */
    public Object getMaleCancerIncidencePer1000() {
        return maleCancerIncidencePer1000;
    }

    /**
     * (Required)
     *
     * @param maleCancerIncidencePer1000 The Male_Cancer_incidence_per_1000_
     */
    public void setMaleCancerIncidencePer1000(Object maleCancerIncidencePer1000) {
        this.maleCancerIncidencePer1000 = maleCancerIncidencePer1000;
    }

    /**
     * (Required)
     *
     * @return The rankMaleCancerIncidencePer
     */
    public Object getRankMaleCancerIncidencePer() {
        return rankMaleCancerIncidencePer;
    }

    /**
     * (Required)
     *
     * @param rankMaleCancerIncidencePer The Rank_Male_Cancer_incidence_per_
     */
    public void setRankMaleCancerIncidencePer(Object rankMaleCancerIncidencePer) {
        this.rankMaleCancerIncidencePer = rankMaleCancerIncidencePer;
    }

    /**
     * (Required)
     *
     * @return The femaleCancerIncidencePer100
     */
    public Object getFemaleCancerIncidencePer100() {
        return femaleCancerIncidencePer100;
    }

    /**
     * (Required)
     *
     * @param femaleCancerIncidencePer100 The Female_Cancer_incidence_per_100
     */
    public void setFemaleCancerIncidencePer100(Object femaleCancerIncidencePer100) {
        this.femaleCancerIncidencePer100 = femaleCancerIncidencePer100;
    }

    /**
     * (Required)
     *
     * @return The rankFemaleCancerIncidencePe
     */
    public Object getRankFemaleCancerIncidencePe() {
        return rankFemaleCancerIncidencePe;
    }

    /**
     * (Required)
     *
     * @param rankFemaleCancerIncidencePe The Rank_Female_Cancer_incidence_pe
     */
    public void setRankFemaleCancerIncidencePe(Object rankFemaleCancerIncidencePe) {
        this.rankFemaleCancerIncidencePe = rankFemaleCancerIncidencePe;
    }

    /**
     * (Required)
     *
     * @return The percentPoorDentalHealth
     */
    public Object getPercentPoorDentalHealth() {
        return percentPoorDentalHealth;
    }

    /**
     * (Required)
     *
     * @param percentPoorDentalHealth The Percent_Poor_dental_health
     */
    public void setPercentPoorDentalHealth(Object percentPoorDentalHealth) {
        this.percentPoorDentalHealth = percentPoorDentalHealth;
    }

    /**
     * (Required)
     *
     * @return The rankPercentPoorDentalHealth
     */
    public Object getRankPercentPoorDentalHealth() {
        return rankPercentPoorDentalHealth;
    }

    /**
     * (Required)
     *
     * @param rankPercentPoorDentalHealth The Rank_Percent_Poor_dental_health
     */
    public void setRankPercentPoorDentalHealth(Object rankPercentPoorDentalHealth) {
        this.rankPercentPoorDentalHealth = rankPercentPoorDentalHealth;
    }

    /**
     * (Required)
     *
     * @return The notificationsPer100000PopOf
     */
    public Object getNotificationsPer100000PopOf() {
        return notificationsPer100000PopOf;
    }

    /**
     * (Required)
     *
     * @param notificationsPer100000PopOf The Notifications_per_100000_pop_of
     */
    public void setNotificationsPer100000PopOf(Object notificationsPer100000PopOf) {
        this.notificationsPer100000PopOf = notificationsPer100000PopOf;
    }

    /**
     * (Required)
     *
     * @return The rankNotificationsPer100000P
     */
    public Object getRankNotificationsPer100000P() {
        return rankNotificationsPer100000P;
    }

    /**
     * (Required)
     *
     * @param rankNotificationsPer100000P The Rank_Notifications_per_100000_p
     */
    public void setRankNotificationsPer100000P(Object rankNotificationsPer100000P) {
        this.rankNotificationsPer100000P = rankNotificationsPer100000P;
    }

    /**
     * (Required)
     *
     * @return The notificationsPer100000Pop1
     */
    public Object getNotificationsPer100000Pop1() {
        return notificationsPer100000Pop1;
    }

    /**
     * (Required)
     *
     * @param notificationsPer100000Pop1 The Notifications_per_100000_pop__1
     */
    public void setNotificationsPer100000Pop1(Object notificationsPer100000Pop1) {
        this.notificationsPer100000Pop1 = notificationsPer100000Pop1;
    }

    /**
     * (Required)
     *
     * @return The rankNotificationsPer1000001
     */
    public Object getRankNotificationsPer1000001() {
        return rankNotificationsPer1000001;
    }

    /**
     * (Required)
     *
     * @param rankNotificationsPer1000001 The Rank_Notifications_per_100000_1
     */
    public void setRankNotificationsPer1000001(Object rankNotificationsPer1000001) {
        this.rankNotificationsPer1000001 = rankNotificationsPer1000001;
    }

    /**
     * (Required)
     *
     * @return The notificationsPer100000People
     */
    public Object getNotificationsPer100000People() {
        return notificationsPer100000People;
    }

    /**
     * (Required)
     *
     * @param notificationsPer100000People The Notifications_per_100000_people
     */
    public void setNotificationsPer100000People(Object notificationsPer100000People) {
        this.notificationsPer100000People = notificationsPer100000People;
    }

    /**
     * (Required)
     *
     * @return The rankNotificationsPer1000002
     */
    public Object getRankNotificationsPer1000002() {
        return rankNotificationsPer1000002;
    }

    /**
     * (Required)
     *
     * @param rankNotificationsPer1000002 The Rank_Notifications_per_100000_2
     */
    public void setRankNotificationsPer1000002(Object rankNotificationsPer1000002) {
        this.rankNotificationsPer1000002 = rankNotificationsPer1000002;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersons18yrsPlusWh
     */
    public Object getPercentOfPersons18yrsPlusWh() {
        return percentOfPersons18yrsPlusWh;
    }

    /**
     * (Required)
     *
     * @param percentOfPersons18yrsPlusWh The Percent_of_persons_18yrsPlus_wh
     */
    public void setPercentOfPersons18yrsPlusWh(Object percentOfPersons18yrsPlusWh) {
        this.percentOfPersons18yrsPlusWh = percentOfPersons18yrsPlusWh;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersons18yrsPl
     */
    public Object getRankPercentOfPersons18yrsPl() {
        return rankPercentOfPersons18yrsPl;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersons18yrsPl The Rank_Percent_of_persons_18yrsPl
     */
    public void setRankPercentOfPersons18yrsPl(Object rankPercentOfPersons18yrsPl) {
        this.rankPercentOfPersons18yrsPl = rankPercentOfPersons18yrsPl;
    }

    /**
     * (Required)
     *
     * @return The percentOfMales18yrsPlusWho
     */
    public Object getPercentOfMales18yrsPlusWho() {
        return percentOfMales18yrsPlusWho;
    }

    /**
     * (Required)
     *
     * @param percentOfMales18yrsPlusWho The Percent_of_males_18yrsPlus_who_
     */
    public void setPercentOfMales18yrsPlusWho(Object percentOfMales18yrsPlusWho) {
        this.percentOfMales18yrsPlusWho = percentOfMales18yrsPlusWho;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfMales18yrsPlus
     */
    public Object getRankPercentOfMales18yrsPlus() {
        return rankPercentOfMales18yrsPlus;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfMales18yrsPlus The Rank_Percent_of_males_18yrsPlus
     */
    public void setRankPercentOfMales18yrsPlus(Object rankPercentOfMales18yrsPlus) {
        this.rankPercentOfMales18yrsPlus = rankPercentOfMales18yrsPlus;
    }

    /**
     * (Required)
     *
     * @return The percentOfFemales18yrsPlusWh
     */
    public Object getPercentOfFemales18yrsPlusWh() {
        return percentOfFemales18yrsPlusWh;
    }

    /**
     * (Required)
     *
     * @param percentOfFemales18yrsPlusWh The Percent_of_females_18yrsPlus_wh
     */
    public void setPercentOfFemales18yrsPlusWh(Object percentOfFemales18yrsPlusWh) {
        this.percentOfFemales18yrsPlusWh = percentOfFemales18yrsPlusWh;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfFemales18yrsPl
     */
    public Object getRankPercentOfFemales18yrsPl() {
        return rankPercentOfFemales18yrsPl;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfFemales18yrsPl The Rank_Percent_of_females_18yrsPl
     */
    public void setRankPercentOfFemales18yrsPl(Object rankPercentOfFemales18yrsPl) {
        this.rankPercentOfFemales18yrsPl = rankPercentOfFemales18yrsPl;
    }

    /**
     * (Required)
     *
     * @return The consumedAlcoholAtLeastWeekl
     */
    public Object getConsumedAlcoholAtLeastWeekl() {
        return consumedAlcoholAtLeastWeekl;
    }

    /**
     * (Required)
     *
     * @param consumedAlcoholAtLeastWeekl The Consumed_alcohol_at_least_weekl
     */
    public void setConsumedAlcoholAtLeastWeekl(Object consumedAlcoholAtLeastWeekl) {
        this.consumedAlcoholAtLeastWeekl = consumedAlcoholAtLeastWeekl;
    }

    /**
     * (Required)
     *
     * @return The rankConsumedAlcoholAtLeast
     */
    public Object getRankConsumedAlcoholAtLeast() {
        return rankConsumedAlcoholAtLeast;
    }

    /**
     * (Required)
     *
     * @param rankConsumedAlcoholAtLeast The Rank_Consumed_alcohol_at_least_
     */
    public void setRankConsumedAlcoholAtLeast(Object rankConsumedAlcoholAtLeast) {
        this.rankConsumedAlcoholAtLeast = rankConsumedAlcoholAtLeast;
    }

    /**
     * (Required)
     *
     * @return The consumedAlcoholAtLeastWee1
     */
    public Object getConsumedAlcoholAtLeastWee1() {
        return consumedAlcoholAtLeastWee1;
    }

    /**
     * (Required)
     *
     * @param consumedAlcoholAtLeastWee1 The Consumed_alcohol_at_least_wee_1
     */
    public void setConsumedAlcoholAtLeastWee1(Object consumedAlcoholAtLeastWee1) {
        this.consumedAlcoholAtLeastWee1 = consumedAlcoholAtLeastWee1;
    }

    /**
     * (Required)
     *
     * @return The rankConsumedAlcoholAtLeast1
     */
    public Object getRankConsumedAlcoholAtLeast1() {
        return rankConsumedAlcoholAtLeast1;
    }

    /**
     * (Required)
     *
     * @param rankConsumedAlcoholAtLeast1 The Rank_Consumed_alcohol_at_least1
     */
    public void setRankConsumedAlcoholAtLeast1(Object rankConsumedAlcoholAtLeast1) {
        this.rankConsumedAlcoholAtLeast1 = rankConsumedAlcoholAtLeast1;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsWhoDoNotM
     */
    public Object getPercentOfPersonsWhoDoNotM() {
        return percentOfPersonsWhoDoNotM;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsWhoDoNotM The Percent_of_persons_who_do_not_m
     */
    public void setPercentOfPersonsWhoDoNotM(Object percentOfPersonsWhoDoNotM) {
        this.percentOfPersonsWhoDoNotM = percentOfPersonsWhoDoNotM;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsWhoDo
     */
    public Object getRankPercentOfPersonsWhoDo() {
        return rankPercentOfPersonsWhoDo;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsWhoDo The Rank_Percent_of_persons_who_do_
     */
    public void setRankPercentOfPersonsWhoDo(Object rankPercentOfPersonsWhoDo) {
        this.rankPercentOfPersonsWhoDo = rankPercentOfPersonsWhoDo;
    }

    /**
     * (Required)
     *
     * @return The percentOfMalesWhoDoNotMee
     */
    public Object getPercentOfMalesWhoDoNotMee() {
        return percentOfMalesWhoDoNotMee;
    }

    /**
     * (Required)
     *
     * @param percentOfMalesWhoDoNotMee The Percent_of_males_who_do_not_mee
     */
    public void setPercentOfMalesWhoDoNotMee(Object percentOfMalesWhoDoNotMee) {
        this.percentOfMalesWhoDoNotMee = percentOfMalesWhoDoNotMee;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfMalesWhoDoNo
     */
    public Object getRankPercentOfMalesWhoDoNo() {
        return rankPercentOfMalesWhoDoNo;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfMalesWhoDoNo The Rank_Percent_of_males_who_do_no
     */
    public void setRankPercentOfMalesWhoDoNo(Object rankPercentOfMalesWhoDoNo) {
        this.rankPercentOfMalesWhoDoNo = rankPercentOfMalesWhoDoNo;
    }

    /**
     * (Required)
     *
     * @return The percentOfFemalesWhoDoNotM
     */
    public Object getPercentOfFemalesWhoDoNotM() {
        return percentOfFemalesWhoDoNotM;
    }

    /**
     * (Required)
     *
     * @param percentOfFemalesWhoDoNotM The Percent_of_females_who_do_not_m
     */
    public void setPercentOfFemalesWhoDoNotM(Object percentOfFemalesWhoDoNotM) {
        this.percentOfFemalesWhoDoNotM = percentOfFemalesWhoDoNotM;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfFemalesWhoDo
     */
    public Object getRankPercentOfFemalesWhoDo() {
        return rankPercentOfFemalesWhoDo;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfFemalesWhoDo The Rank_Percent_of_females_who_do_
     */
    public void setRankPercentOfFemalesWhoDo(Object rankPercentOfFemalesWhoDo) {
        this.rankPercentOfFemalesWhoDo = rankPercentOfFemalesWhoDo;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsWhoDrinkSo
     */
    public Object getPercentOfPersonsWhoDrinkSo() {
        return percentOfPersonsWhoDrinkSo;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsWhoDrinkSo The Percent_of_persons_who_drink_so
     */
    public void setPercentOfPersonsWhoDrinkSo(Object percentOfPersonsWhoDrinkSo) {
        this.percentOfPersonsWhoDrinkSo = percentOfPersonsWhoDrinkSo;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsWhoDri
     */
    public Object getRankPercentOfPersonsWhoDri() {
        return rankPercentOfPersonsWhoDri;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsWhoDri The Rank_Percent_of_persons_who_dri
     */
    public void setRankPercentOfPersonsWhoDri(Object rankPercentOfPersonsWhoDri) {
        this.rankPercentOfPersonsWhoDri = rankPercentOfPersonsWhoDri;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsWhoShareA
     */
    public Object getPercentOfPersonsWhoShareA() {
        return percentOfPersonsWhoShareA;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsWhoShareA The Percent_of_persons_who_share_a_
     */
    public void setPercentOfPersonsWhoShareA(Object percentOfPersonsWhoShareA) {
        this.percentOfPersonsWhoShareA = percentOfPersonsWhoShareA;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsWhoSha
     */
    public Object getRankPercentOfPersonsWhoSha() {
        return rankPercentOfPersonsWhoSha;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsWhoSha The Rank_Percent_of_persons_who_sha
     */
    public void setRankPercentOfPersonsWhoSha(Object rankPercentOfPersonsWhoSha) {
        this.rankPercentOfPersonsWhoSha = rankPercentOfPersonsWhoSha;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsWhoDoNot1
     */
    public Object getPercentOfPersonsWhoDoNot1() {
        return percentOfPersonsWhoDoNot1;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsWhoDoNot1 The Percent_of_persons_who_do_not_1
     */
    public void setPercentOfPersonsWhoDoNot1(Object percentOfPersonsWhoDoNot1) {
        this.percentOfPersonsWhoDoNot1 = percentOfPersonsWhoDoNot1;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsWhoDo1
     */
    public Object getRankPercentOfPersonsWhoDo1() {
        return rankPercentOfPersonsWhoDo1;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsWhoDo1 The Rank_Percent_of_persons_who_do1
     */
    public void setRankPercentOfPersonsWhoDo1(Object rankPercentOfPersonsWhoDo1) {
        this.rankPercentOfPersonsWhoDo1 = rankPercentOfPersonsWhoDo1;
    }

    /**
     * (Required)
     *
     * @return The percentOfMalesWhoDoNotM1
     */
    public Object getPercentOfMalesWhoDoNotM1() {
        return percentOfMalesWhoDoNotM1;
    }

    /**
     * (Required)
     *
     * @param percentOfMalesWhoDoNotM1 The Percent_of_males_who_do_not_m_1
     */
    public void setPercentOfMalesWhoDoNotM1(Object percentOfMalesWhoDoNotM1) {
        this.percentOfMalesWhoDoNotM1 = percentOfMalesWhoDoNotM1;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfMalesWhoDo1
     */
    public Object getRankPercentOfMalesWhoDo1() {
        return rankPercentOfMalesWhoDo1;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfMalesWhoDo1 The Rank_Percent_of_males_who_do__1
     */
    public void setRankPercentOfMalesWhoDo1(Object rankPercentOfMalesWhoDo1) {
        this.rankPercentOfMalesWhoDo1 = rankPercentOfMalesWhoDo1;
    }

    /**
     * (Required)
     *
     * @return The percentOfFemalesWhoDoNot1
     */
    public Object getPercentOfFemalesWhoDoNot1() {
        return percentOfFemalesWhoDoNot1;
    }

    /**
     * (Required)
     *
     * @param percentOfFemalesWhoDoNot1 The Percent_of_females_who_do_not_1
     */
    public void setPercentOfFemalesWhoDoNot1(Object percentOfFemalesWhoDoNot1) {
        this.percentOfFemalesWhoDoNot1 = percentOfFemalesWhoDoNot1;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfFemalesWhoDo1
     */
    public Object getRankPercentOfFemalesWhoDo1() {
        return rankPercentOfFemalesWhoDo1;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfFemalesWhoDo1 The Rank_Percent_of_females_who_do1
     */
    public void setRankPercentOfFemalesWhoDo1(Object rankPercentOfFemalesWhoDo1) {
        this.rankPercentOfFemalesWhoDo1 = rankPercentOfFemalesWhoDo1;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsWhoSitFor
     */
    public Object getPercentOfPersonsWhoSitFor() {
        return percentOfPersonsWhoSitFor;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsWhoSitFor The Percent_of_persons_who_sit_for_
     */
    public void setPercentOfPersonsWhoSitFor(Object percentOfPersonsWhoSitFor) {
        this.percentOfPersonsWhoSitFor = percentOfPersonsWhoSitFor;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsWhoSit
     */
    public Object getRankPercentOfPersonsWhoSit() {
        return rankPercentOfPersonsWhoSit;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsWhoSit The Rank_Percent_of_persons_who_sit
     */
    public void setRankPercentOfPersonsWhoSit(Object rankPercentOfPersonsWhoSit) {
        this.rankPercentOfPersonsWhoSit = rankPercentOfPersonsWhoSit;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsWhoVisitA
     */
    public Object getPercentOfPersonsWhoVisitA() {
        return percentOfPersonsWhoVisitA;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsWhoVisitA The Percent_of_persons_who_visit_a_
     */
    public void setPercentOfPersonsWhoVisitA(Object percentOfPersonsWhoVisitA) {
        this.percentOfPersonsWhoVisitA = percentOfPersonsWhoVisitA;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsWhoVis
     */
    public Object getRankPercentOfPersonsWhoVis() {
        return rankPercentOfPersonsWhoVis;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsWhoVis The Rank_Percent_of_persons_who_vis
     */
    public void setRankPercentOfPersonsWhoVis(Object rankPercentOfPersonsWhoVis) {
        this.rankPercentOfPersonsWhoVis = rankPercentOfPersonsWhoVis;
    }

    /**
     * (Required)
     *
     * @return The percentOfBreastScreeningPar
     */
    public Object getPercentOfBreastScreeningPar() {
        return percentOfBreastScreeningPar;
    }

    /**
     * (Required)
     *
     * @param percentOfBreastScreeningPar The Percent_of_breast_screening_par
     */
    public void setPercentOfBreastScreeningPar(Object percentOfBreastScreeningPar) {
        this.percentOfBreastScreeningPar = percentOfBreastScreeningPar;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfBreastScreenin
     */
    public Object getRankPercentOfBreastScreenin() {
        return rankPercentOfBreastScreenin;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfBreastScreenin The Rank_Percent_of_breast_screenin
     */
    public void setRankPercentOfBreastScreenin(Object rankPercentOfBreastScreenin) {
        this.rankPercentOfBreastScreenin = rankPercentOfBreastScreenin;
    }

    /**
     * (Required)
     *
     * @return The percentOfCervicalCancerScre
     */
    public Object getPercentOfCervicalCancerScre() {
        return percentOfCervicalCancerScre;
    }

    /**
     * (Required)
     *
     * @param percentOfCervicalCancerScre The Percent_of_cervical_cancer_scre
     */
    public void setPercentOfCervicalCancerScre(Object percentOfCervicalCancerScre) {
        this.percentOfCervicalCancerScre = percentOfCervicalCancerScre;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfCervicalCancer
     */
    public Object getRankPercentOfCervicalCancer() {
        return rankPercentOfCervicalCancer;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfCervicalCancer The Rank_Percent_of_cervical_cancer
     */
    public void setRankPercentOfCervicalCancer(Object rankPercentOfCervicalCancer) {
        this.rankPercentOfCervicalCancer = rankPercentOfCervicalCancer;
    }

    /**
     * (Required)
     *
     * @return The bowelCancerScreeningParticip
     */
    public Object getBowelCancerScreeningParticip() {
        return bowelCancerScreeningParticip;
    }

    /**
     * (Required)
     *
     * @param bowelCancerScreeningParticip The Bowel_cancer_screening_particip
     */
    public void setBowelCancerScreeningParticip(Object bowelCancerScreeningParticip) {
        this.bowelCancerScreeningParticip = bowelCancerScreeningParticip;
    }

    /**
     * (Required)
     *
     * @return The rankBowelCancerScreeningPar
     */
    public Object getRankBowelCancerScreeningPar() {
        return rankBowelCancerScreeningPar;
    }

    /**
     * (Required)
     *
     * @param rankBowelCancerScreeningPar The Rank_Bowel_cancer_screening_par
     */
    public void setRankBowelCancerScreeningPar(Object rankBowelCancerScreeningPar) {
        this.rankBowelCancerScreeningPar = rankBowelCancerScreeningPar;
    }

    /**
     * (Required)
     *
     * @return The bowelCancerScreeningPartic1
     */
    public Object getBowelCancerScreeningPartic1() {
        return bowelCancerScreeningPartic1;
    }

    /**
     * (Required)
     *
     * @param bowelCancerScreeningPartic1 The Bowel_cancer_screening_partic_1
     */
    public void setBowelCancerScreeningPartic1(Object bowelCancerScreeningPartic1) {
        this.bowelCancerScreeningPartic1 = bowelCancerScreeningPartic1;
    }

    /**
     * (Required)
     *
     * @return The rankBowelCancerScreeningP1
     */
    public Object getRankBowelCancerScreeningP1() {
        return rankBowelCancerScreeningP1;
    }

    /**
     * (Required)
     *
     * @param rankBowelCancerScreeningP1 The Rank_Bowel_cancer_screening_p_1
     */
    public void setRankBowelCancerScreeningP1(Object rankBowelCancerScreeningP1) {
        this.rankBowelCancerScreeningP1 = rankBowelCancerScreeningP1;
    }

    /**
     * (Required)
     *
     * @return The bowelCancerScreeningPartic2
     */
    public Object getBowelCancerScreeningPartic2() {
        return bowelCancerScreeningPartic2;
    }

    /**
     * (Required)
     *
     * @param bowelCancerScreeningPartic2 The Bowel_cancer_screening_partic_2
     */
    public void setBowelCancerScreeningPartic2(Object bowelCancerScreeningPartic2) {
        this.bowelCancerScreeningPartic2 = bowelCancerScreeningPartic2;
    }

    /**
     * (Required)
     *
     * @return The rankBowelCancerScreeningP2
     */
    public Object getRankBowelCancerScreeningP2() {
        return rankBowelCancerScreeningP2;
    }

    /**
     * (Required)
     *
     * @param rankBowelCancerScreeningP2 The Rank_Bowel_cancer_screening_p_2
     */
    public void setRankBowelCancerScreeningP2(Object rankBowelCancerScreeningP2) {
        this.rankBowelCancerScreeningP2 = rankBowelCancerScreeningP2;
    }

    /**
     * (Required)
     *
     * @return The lowBirthweightBabies
     */
    public Object getLowBirthweightBabies() {
        return lowBirthweightBabies;
    }

    /**
     * (Required)
     *
     * @param lowBirthweightBabies The Low_Birthweight_babies
     */
    public void setLowBirthweightBabies(Object lowBirthweightBabies) {
        this.lowBirthweightBabies = lowBirthweightBabies;
    }

    /**
     * (Required)
     *
     * @return The rankLowBirthweightBabies
     */
    public Object getRankLowBirthweightBabies() {
        return rankLowBirthweightBabies;
    }

    /**
     * (Required)
     *
     * @param rankLowBirthweightBabies The Rank_Low_Birthweight_babies
     */
    public void setRankLowBirthweightBabies(Object rankLowBirthweightBabies) {
        this.rankLowBirthweightBabies = rankLowBirthweightBabies;
    }

    /**
     * (Required)
     *
     * @return The percentInfantsFullyBreastfed
     */
    public Object getPercentInfantsFullyBreastfed() {
        return percentInfantsFullyBreastfed;
    }

    /**
     * (Required)
     *
     * @param percentInfantsFullyBreastfed The Percent_Infants_fully_breastfed
     */
    public void setPercentInfantsFullyBreastfed(Object percentInfantsFullyBreastfed) {
        this.percentInfantsFullyBreastfed = percentInfantsFullyBreastfed;
    }

    /**
     * (Required)
     *
     * @return The rankPercentInfantsFullyBrea
     */
    public Object getRankPercentInfantsFullyBrea() {
        return rankPercentInfantsFullyBrea;
    }

    /**
     * (Required)
     *
     * @param rankPercentInfantsFullyBrea The Rank_Percent_Infants_fully_brea
     */
    public void setRankPercentInfantsFullyBrea(Object rankPercentInfantsFullyBrea) {
        this.rankPercentInfantsFullyBrea = rankPercentInfantsFullyBrea;
    }

    /**
     * (Required)
     *
     * @return The percentChildrenFullyImmunise
     */
    public Object getPercentChildrenFullyImmunise() {
        return percentChildrenFullyImmunise;
    }

    /**
     * (Required)
     *
     * @param percentChildrenFullyImmunise The Percent_Children_fully_immunise
     */
    public void setPercentChildrenFullyImmunise(Object percentChildrenFullyImmunise) {
        this.percentChildrenFullyImmunise = percentChildrenFullyImmunise;
    }

    /**
     * (Required)
     *
     * @return The rankPercentChildrenFullyImm
     */
    public Object getRankPercentChildrenFullyImm() {
        return rankPercentChildrenFullyImm;
    }

    /**
     * (Required)
     *
     * @param rankPercentChildrenFullyImm The Rank_Percent_Children_fully_imm
     */
    public void setRankPercentChildrenFullyImm(Object rankPercentChildrenFullyImm) {
        this.rankPercentChildrenFullyImm = rankPercentChildrenFullyImm;
    }

    /**
     * (Required)
     *
     * @return The proportionOfInfantsEnrolled
     */
    public Object getProportionOfInfantsEnrolled() {
        return proportionOfInfantsEnrolled;
    }

    /**
     * (Required)
     *
     * @param proportionOfInfantsEnrolled The Proportion_of_infants_enrolled_
     */
    public void setProportionOfInfantsEnrolled(Object proportionOfInfantsEnrolled) {
        this.proportionOfInfantsEnrolled = proportionOfInfantsEnrolled;
    }

    /**
     * (Required)
     *
     * @return The rankProportionOfInfantsEnro
     */
    public Object getRankProportionOfInfantsEnro() {
        return rankProportionOfInfantsEnro;
    }

    /**
     * (Required)
     *
     * @param rankProportionOfInfantsEnro The Rank_Proportion_of_infants_enro
     */
    public void setRankProportionOfInfantsEnro(Object rankProportionOfInfantsEnro) {
        this.rankProportionOfInfantsEnro = rankProportionOfInfantsEnro;
    }

    /**
     * (Required)
     *
     * @return The kindergartenParticipationRate
     */
    public Object getKindergartenParticipationRate() {
        return kindergartenParticipationRate;
    }

    /**
     * (Required)
     *
     * @param kindergartenParticipationRate The Kindergarten_participation_rate
     */
    public void setKindergartenParticipationRate(Object kindergartenParticipationRate) {
        this.kindergartenParticipationRate = kindergartenParticipationRate;
    }

    /**
     * (Required)
     *
     * @return The percentOfChildrenWithKinder
     */
    public Object getPercentOfChildrenWithKinder() {
        return percentOfChildrenWithKinder;
    }

    /**
     * (Required)
     *
     * @param percentOfChildrenWithKinder The Percent_of_children_with_kinder
     */
    public void setPercentOfChildrenWithKinder(Object percentOfChildrenWithKinder) {
        this.percentOfChildrenWithKinder = percentOfChildrenWithKinder;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfChildrenWithK
     */
    public Object getRankPercentOfChildrenWithK() {
        return rankPercentOfChildrenWithK;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfChildrenWithK The Rank_Percent_of_children_with_k
     */
    public void setRankPercentOfChildrenWithK(Object rankPercentOfChildrenWithK) {
        this.rankPercentOfChildrenWithK = rankPercentOfChildrenWithK;
    }

    /**
     * (Required)
     *
     * @return The percentOfChildrenWithEmotio
     */
    public Object getPercentOfChildrenWithEmotio() {
        return percentOfChildrenWithEmotio;
    }

    /**
     * (Required)
     *
     * @param percentOfChildrenWithEmotio The Percent_of_children_with_emotio
     */
    public void setPercentOfChildrenWithEmotio(Object percentOfChildrenWithEmotio) {
        this.percentOfChildrenWithEmotio = percentOfChildrenWithEmotio;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfChildrenWithE
     */
    public Object getRankPercentOfChildrenWithE() {
        return rankPercentOfChildrenWithE;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfChildrenWithE The Rank_Percent_of_children_with_e
     */
    public void setRankPercentOfChildrenWithE(Object rankPercentOfChildrenWithE) {
        this.rankPercentOfChildrenWithE = rankPercentOfChildrenWithE;
    }

    /**
     * (Required)
     *
     * @return The percentOfChildrenWithSpeech
     */
    public Object getPercentOfChildrenWithSpeech() {
        return percentOfChildrenWithSpeech;
    }

    /**
     * (Required)
     *
     * @param percentOfChildrenWithSpeech The Percent_of_children_with_speech
     */
    public void setPercentOfChildrenWithSpeech(Object percentOfChildrenWithSpeech) {
        this.percentOfChildrenWithSpeech = percentOfChildrenWithSpeech;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfChildrenWithS
     */
    public Object getRankPercentOfChildrenWithS() {
        return rankPercentOfChildrenWithS;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfChildrenWithS The Rank_Percent_of_children_with_s
     */
    public void setRankPercentOfChildrenWithS(Object rankPercentOfChildrenWithS) {
        this.rankPercentOfChildrenWithS = rankPercentOfChildrenWithS;
    }

    /**
     * (Required)
     *
     * @return The percentOfAdolescentsWhoRepo
     */
    public Object getPercentOfAdolescentsWhoRepo() {
        return percentOfAdolescentsWhoRepo;
    }

    /**
     * (Required)
     *
     * @param percentOfAdolescentsWhoRepo The Percent_of_adolescents_who_repo
     */
    public void setPercentOfAdolescentsWhoRepo(Object percentOfAdolescentsWhoRepo) {
        this.percentOfAdolescentsWhoRepo = percentOfAdolescentsWhoRepo;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfAdolescentsWho
     */
    public Object getRankPercentOfAdolescentsWho() {
        return rankPercentOfAdolescentsWho;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfAdolescentsWho The Rank_Percent_of_adolescents_who
     */
    public void setRankPercentOfAdolescentsWho(Object rankPercentOfAdolescentsWho) {
        this.rankPercentOfAdolescentsWho = rankPercentOfAdolescentsWho;
    }

    /**
     * (Required)
     *
     * @return The percentOfChildrenWhoAreDev
     */
    public Object getPercentOfChildrenWhoAreDev() {
        return percentOfChildrenWhoAreDev;
    }

    /**
     * (Required)
     *
     * @param percentOfChildrenWhoAreDev The Percent_of_children_who_are_dev
     */
    public void setPercentOfChildrenWhoAreDev(Object percentOfChildrenWhoAreDev) {
        this.percentOfChildrenWhoAreDev = percentOfChildrenWhoAreDev;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfChildrenWhoAr
     */
    public Object getRankPercentOfChildrenWhoAr() {
        return rankPercentOfChildrenWhoAr;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfChildrenWhoAr The Rank_Percent_of_children_who_ar
     */
    public void setRankPercentOfChildrenWhoAr(Object rankPercentOfChildrenWhoAr) {
        this.rankPercentOfChildrenWhoAr = rankPercentOfChildrenWhoAr;
    }

    /**
     * (Required)
     *
     * @return The percentOfChildrenWhoAreD1
     */
    public Object getPercentOfChildrenWhoAreD1() {
        return percentOfChildrenWhoAreD1;
    }

    /**
     * (Required)
     *
     * @param percentOfChildrenWhoAreD1 The Percent_of_children_who_are_d_1
     */
    public void setPercentOfChildrenWhoAreD1(Object percentOfChildrenWhoAreD1) {
        this.percentOfChildrenWhoAreD1 = percentOfChildrenWhoAreD1;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfChildrenWho1
     */
    public Object getRankPercentOfChildrenWho1() {
        return rankPercentOfChildrenWho1;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfChildrenWho1 The Rank_Percent_of_children_who__1
     */
    public void setRankPercentOfChildrenWho1(Object rankPercentOfChildrenWho1) {
        this.rankPercentOfChildrenWho1 = rankPercentOfChildrenWho1;
    }

    /**
     * (Required)
     *
     * @return The coreActivityNeedForAssistan
     */
    public Object getCoreActivityNeedForAssistan() {
        return coreActivityNeedForAssistan;
    }

    /**
     * (Required)
     *
     * @param coreActivityNeedForAssistan The Core_activity_need_for_assistan
     */
    public void setCoreActivityNeedForAssistan(Object coreActivityNeedForAssistan) {
        this.coreActivityNeedForAssistan = coreActivityNeedForAssistan;
    }

    /**
     * (Required)
     *
     * @return The rankCoreActivityNeedForAss
     */
    public Object getRankCoreActivityNeedForAss() {
        return rankCoreActivityNeedForAss;
    }

    /**
     * (Required)
     *
     * @param rankCoreActivityNeedForAss The Rank_Core_activity_need_for_ass
     */
    public void setRankCoreActivityNeedForAss(Object rankCoreActivityNeedForAss) {
        this.rankCoreActivityNeedForAss = rankCoreActivityNeedForAss;
    }

    /**
     * (Required)
     *
     * @return The peopleWithSevereAndProfound
     */
    public Object getPeopleWithSevereAndProfound() {
        return peopleWithSevereAndProfound;
    }

    /**
     * (Required)
     *
     * @param peopleWithSevereAndProfound The People_with_severe_and_profound
     */
    public void setPeopleWithSevereAndProfound(Object peopleWithSevereAndProfound) {
        this.peopleWithSevereAndProfound = peopleWithSevereAndProfound;
    }

    /**
     * (Required)
     *
     * @return The rankPeopleWithSevereAndPro
     */
    public Object getRankPeopleWithSevereAndPro() {
        return rankPeopleWithSevereAndPro;
    }

    /**
     * (Required)
     *
     * @param rankPeopleWithSevereAndPro The Rank_People_with_severe_and_pro
     */
    public void setRankPeopleWithSevereAndPro(Object rankPeopleWithSevereAndPro) {
        this.rankPeopleWithSevereAndPro = rankPeopleWithSevereAndPro;
    }

    /**
     * (Required)
     *
     * @return The peopleWithSevereAndProfou1
     */
    public Object getPeopleWithSevereAndProfou1() {
        return peopleWithSevereAndProfou1;
    }

    /**
     * (Required)
     *
     * @param peopleWithSevereAndProfou1 The People_with_severe_and_profou_1
     */
    public void setPeopleWithSevereAndProfou1(Object peopleWithSevereAndProfou1) {
        this.peopleWithSevereAndProfou1 = peopleWithSevereAndProfou1;
    }

    /**
     * (Required)
     *
     * @return The rankPeopleWithSevereAndP1
     */
    public Object getRankPeopleWithSevereAndP1() {
        return rankPeopleWithSevereAndP1;
    }

    /**
     * (Required)
     *
     * @param rankPeopleWithSevereAndP1 The Rank_People_with_severe_and_p_1
     */
    public void setRankPeopleWithSevereAndP1(Object rankPeopleWithSevereAndP1) {
        this.rankPeopleWithSevereAndP1 = rankPeopleWithSevereAndP1;
    }

    /**
     * (Required)
     *
     * @return The percentPopAged75yrsPlusLivi
     */
    public Object getPercentPopAged75yrsPlusLivi() {
        return percentPopAged75yrsPlusLivi;
    }

    /**
     * (Required)
     *
     * @param percentPopAged75yrsPlusLivi The Percent_pop_aged_75yrsPlus_livi
     */
    public void setPercentPopAged75yrsPlusLivi(Object percentPopAged75yrsPlusLivi) {
        this.percentPopAged75yrsPlusLivi = percentPopAged75yrsPlusLivi;
    }

    /**
     * (Required)
     *
     * @return The rankPercentPopAged75yrsPlus
     */
    public Object getRankPercentPopAged75yrsPlus() {
        return rankPercentPopAged75yrsPlus;
    }

    /**
     * (Required)
     *
     * @param rankPercentPopAged75yrsPlus The Rank_Percent_pop_aged_75yrsPlus
     */
    public void setRankPercentPopAged75yrsPlus(Object rankPercentPopAged75yrsPlus) {
        this.rankPercentPopAged75yrsPlus = rankPercentPopAged75yrsPlus;
    }

    /**
     * (Required)
     *
     * @return The popAged75yrsPlusLivingAlone
     */
    public Object getPopAged75yrsPlusLivingAlone() {
        return popAged75yrsPlusLivingAlone;
    }

    /**
     * (Required)
     *
     * @param popAged75yrsPlusLivingAlone The Pop_aged_75yrsPlus_living_alone
     */
    public void setPopAged75yrsPlusLivingAlone(Object popAged75yrsPlusLivingAlone) {
        this.popAged75yrsPlusLivingAlone = popAged75yrsPlusLivingAlone;
    }

    /**
     * (Required)
     *
     * @return The rankPopAged75yrsPlusLiving
     */
    public Object getRankPopAged75yrsPlusLiving() {
        return rankPopAged75yrsPlusLiving;
    }

    /**
     * (Required)
     *
     * @param rankPopAged75yrsPlusLiving The Rank_Pop_aged_75yrsPlus_living_
     */
    public void setRankPopAged75yrsPlusLiving(Object rankPopAged75yrsPlusLiving) {
        this.rankPopAged75yrsPlusLiving = rankPopAged75yrsPlusLiving;
    }

    /**
     * (Required)
     *
     * @return The popAged75yrsPlusLivingAlo1
     */
    public Object getPopAged75yrsPlusLivingAlo1() {
        return popAged75yrsPlusLivingAlo1;
    }

    /**
     * (Required)
     *
     * @param popAged75yrsPlusLivingAlo1 The Pop_aged_75yrsPlus_living_alo_1
     */
    public void setPopAged75yrsPlusLivingAlo1(Object popAged75yrsPlusLivingAlo1) {
        this.popAged75yrsPlusLivingAlo1 = popAged75yrsPlusLivingAlo1;
    }

    /**
     * (Required)
     *
     * @return The rankPopAged75yrsPlusLiving1
     */
    public Object getRankPopAged75yrsPlusLiving1() {
        return rankPopAged75yrsPlusLiving1;
    }

    /**
     * (Required)
     *
     * @param rankPopAged75yrsPlusLiving1 The Rank_Pop_aged_75yrsPlus_living1
     */
    public void setRankPopAged75yrsPlusLiving1(Object rankPopAged75yrsPlusLiving1) {
        this.rankPopAged75yrsPlusLiving1 = rankPopAged75yrsPlusLiving1;
    }

    /**
     * (Required)
     *
     * @return The personsReceivingDisabilitySe
     */
    public Object getPersonsReceivingDisabilitySe() {
        return personsReceivingDisabilitySe;
    }

    /**
     * (Required)
     *
     * @param personsReceivingDisabilitySe The Persons_receiving_Disability_Se
     */
    public void setPersonsReceivingDisabilitySe(Object personsReceivingDisabilitySe) {
        this.personsReceivingDisabilitySe = personsReceivingDisabilitySe;
    }

    /**
     * (Required)
     *
     * @return The rankPersonsReceivingDisabili
     */
    public Object getRankPersonsReceivingDisabili() {
        return rankPersonsReceivingDisabili;
    }

    /**
     * (Required)
     *
     * @param rankPersonsReceivingDisabili The Rank_Persons_receiving_Disabili
     */
    public void setRankPersonsReceivingDisabili(Object rankPersonsReceivingDisabili) {
        this.rankPersonsReceivingDisabili = rankPersonsReceivingDisabili;
    }

    /**
     * (Required)
     *
     * @return The disabilityPensionPer1000Eli
     */
    public Object getDisabilityPensionPer1000Eli() {
        return disabilityPensionPer1000Eli;
    }

    /**
     * (Required)
     *
     * @param disabilityPensionPer1000Eli The Disability_pension_per_1000_eli
     */
    public void setDisabilityPensionPer1000Eli(Object disabilityPensionPer1000Eli) {
        this.disabilityPensionPer1000Eli = disabilityPensionPer1000Eli;
    }

    /**
     * (Required)
     *
     * @return The rankDisabilityPensionPer100
     */
    public Object getRankDisabilityPensionPer100() {
        return rankDisabilityPensionPer100;
    }

    /**
     * (Required)
     *
     * @param rankDisabilityPensionPer100 The Rank_Disability_pension_per_100
     */
    public void setRankDisabilityPensionPer100(Object rankDisabilityPensionPer100) {
        this.rankDisabilityPensionPer100 = rankDisabilityPensionPer100;
    }

    /**
     * (Required)
     *
     * @return The agedCareHighCareBeds
     */
    public Object getAgedCareHighCareBeds() {
        return agedCareHighCareBeds;
    }

    /**
     * (Required)
     *
     * @param agedCareHighCareBeds The Aged_care_HighCare_beds
     */
    public void setAgedCareHighCareBeds(Object agedCareHighCareBeds) {
        this.agedCareHighCareBeds = agedCareHighCareBeds;
    }

    /**
     * (Required)
     *
     * @return The agedCareLowCareBeds
     */
    public Object getAgedCareLowCareBeds() {
        return agedCareLowCareBeds;
    }

    /**
     * (Required)
     *
     * @param agedCareLowCareBeds The Aged_care_LowCare_beds
     */
    public void setAgedCareLowCareBeds(Object agedCareLowCareBeds) {
        this.agedCareLowCareBeds = agedCareLowCareBeds;
    }

    /**
     * (Required)
     *
     * @return The agePensionPer1000EligibleP
     */
    public Object getAgePensionPer1000EligibleP() {
        return agePensionPer1000EligibleP;
    }

    /**
     * (Required)
     *
     * @param agePensionPer1000EligibleP The Age_pension_per_1000_eligible_p
     */
    public void setAgePensionPer1000EligibleP(Object agePensionPer1000EligibleP) {
        this.agePensionPer1000EligibleP = agePensionPer1000EligibleP;
    }

    /**
     * (Required)
     *
     * @return The rankAgePensionPer1000Eligi
     */
    public Object getRankAgePensionPer1000Eligi() {
        return rankAgePensionPer1000Eligi;
    }

    /**
     * (Required)
     *
     * @param rankAgePensionPer1000Eligi The Rank_Age_pension_per_1000_eligi
     */
    public void setRankAgePensionPer1000Eligi(Object rankAgePensionPer1000Eligi) {
        this.rankAgePensionPer1000Eligi = rankAgePensionPer1000Eligi;
    }

    /**
     * (Required)
     *
     * @return The maleLifeExpectancy
     */
    public Object getMaleLifeExpectancy() {
        return maleLifeExpectancy;
    }

    /**
     * (Required)
     *
     * @param maleLifeExpectancy The Male_life_expectancy
     */
    public void setMaleLifeExpectancy(Object maleLifeExpectancy) {
        this.maleLifeExpectancy = maleLifeExpectancy;
    }

    /**
     * (Required)
     *
     * @return The rankMaleLifeExpectancy
     */
    public Object getRankMaleLifeExpectancy() {
        return rankMaleLifeExpectancy;
    }

    /**
     * (Required)
     *
     * @param rankMaleLifeExpectancy The Rank_Male_life_expectancy
     */
    public void setRankMaleLifeExpectancy(Object rankMaleLifeExpectancy) {
        this.rankMaleLifeExpectancy = rankMaleLifeExpectancy;
    }

    /**
     * (Required)
     *
     * @return The femaleLifeExpectancy
     */
    public Object getFemaleLifeExpectancy() {
        return femaleLifeExpectancy;
    }

    /**
     * (Required)
     *
     * @param femaleLifeExpectancy The Female_life_expectancy
     */
    public void setFemaleLifeExpectancy(Object femaleLifeExpectancy) {
        this.femaleLifeExpectancy = femaleLifeExpectancy;
    }

    /**
     * (Required)
     *
     * @return The rankFemaleLifeExpectancy
     */
    public Object getRankFemaleLifeExpectancy() {
        return rankFemaleLifeExpectancy;
    }

    /**
     * (Required)
     *
     * @param rankFemaleLifeExpectancy The Rank_Female_life_expectancy
     */
    public void setRankFemaleLifeExpectancy(Object rankFemaleLifeExpectancy) {
        this.rankFemaleLifeExpectancy = rankFemaleLifeExpectancy;
    }

    /**
     * (Required)
     *
     * @return The personsReportingFairOrPoor
     */
    public Object getPersonsReportingFairOrPoor() {
        return personsReportingFairOrPoor;
    }

    /**
     * (Required)
     *
     * @param personsReportingFairOrPoor The Persons_reporting_fair_or_poor_
     */
    public void setPersonsReportingFairOrPoor(Object personsReportingFairOrPoor) {
        this.personsReportingFairOrPoor = personsReportingFairOrPoor;
    }

    /**
     * (Required)
     *
     * @return The rankPersonsReportingFairOr
     */
    public Object getRankPersonsReportingFairOr() {
        return rankPersonsReportingFairOr;
    }

    /**
     * (Required)
     *
     * @param rankPersonsReportingFairOr The Rank_Persons_reporting_fair_or_
     */
    public void setRankPersonsReportingFairOr(Object rankPersonsReportingFairOr) {
        this.rankPersonsReportingFairOr = rankPersonsReportingFairOr;
    }

    /**
     * (Required)
     *
     * @return The femalesReportingFairOrPoor
     */
    public Object getFemalesReportingFairOrPoor() {
        return femalesReportingFairOrPoor;
    }

    /**
     * (Required)
     *
     * @param femalesReportingFairOrPoor The Females_reporting_fair_or_poor_
     */
    public void setFemalesReportingFairOrPoor(Object femalesReportingFairOrPoor) {
        this.femalesReportingFairOrPoor = femalesReportingFairOrPoor;
    }

    /**
     * (Required)
     *
     * @return The rankFemalesReportingFairOr
     */
    public Object getRankFemalesReportingFairOr() {
        return rankFemalesReportingFairOr;
    }

    /**
     * (Required)
     *
     * @param rankFemalesReportingFairOr The Rank_Females_reporting_fair_or_
     */
    public void setRankFemalesReportingFairOr(Object rankFemalesReportingFairOr) {
        this.rankFemalesReportingFairOr = rankFemalesReportingFairOr;
    }

    /**
     * (Required)
     *
     * @return The malesReportingFairOrPoorHe
     */
    public Object getMalesReportingFairOrPoorHe() {
        return malesReportingFairOrPoorHe;
    }

    /**
     * (Required)
     *
     * @param malesReportingFairOrPoorHe The Males_reporting_fair_or_poor_he
     */
    public void setMalesReportingFairOrPoorHe(Object malesReportingFairOrPoorHe) {
        this.malesReportingFairOrPoorHe = malesReportingFairOrPoorHe;
    }

    /**
     * (Required)
     *
     * @return The rankMalesReportingFairOrPo
     */
    public Object getRankMalesReportingFairOrPo() {
        return rankMalesReportingFairOrPo;
    }

    /**
     * (Required)
     *
     * @param rankMalesReportingFairOrPo The Rank_Males_reporting_fair_or_po
     */
    public void setRankMalesReportingFairOrPo(Object rankMalesReportingFairOrPo) {
        this.rankMalesReportingFairOrPo = rankMalesReportingFairOrPo;
    }

    /**
     * (Required)
     *
     * @return The percentWhoHaveAHighDegree
     */
    public Object getPercentWhoHaveAHighDegree() {
        return percentWhoHaveAHighDegree;
    }

    /**
     * (Required)
     *
     * @param percentWhoHaveAHighDegree The Percent_who_have_a_high_degree_
     */
    public void setPercentWhoHaveAHighDegree(Object percentWhoHaveAHighDegree) {
        this.percentWhoHaveAHighDegree = percentWhoHaveAHighDegree;
    }

    /**
     * (Required)
     *
     * @return The rankPercentWhoHaveAHighDe
     */
    public Object getRankPercentWhoHaveAHighDe() {
        return rankPercentWhoHaveAHighDe;
    }

    /**
     * (Required)
     *
     * @param rankPercentWhoHaveAHighDe The Rank_Percent_who_have_a_high_de
     */
    public void setRankPercentWhoHaveAHighDe(Object rankPercentWhoHaveAHighDe) {
        this.rankPercentWhoHaveAHighDe = rankPercentWhoHaveAHighDe;
    }

    /**
     * (Required)
     *
     * @return The percentOfPersonsSleepingLes
     */
    public Object getPercentOfPersonsSleepingLes() {
        return percentOfPersonsSleepingLes;
    }

    /**
     * (Required)
     *
     * @param percentOfPersonsSleepingLes The Percent_of_persons_sleeping_les
     */
    public void setPercentOfPersonsSleepingLes(Object percentOfPersonsSleepingLes) {
        this.percentOfPersonsSleepingLes = percentOfPersonsSleepingLes;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfPersonsSleepin
     */
    public Object getRankPercentOfPersonsSleepin() {
        return rankPercentOfPersonsSleepin;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfPersonsSleepin The Rank_Percent_of_persons_sleepin
     */
    public void setRankPercentOfPersonsSleepin(Object rankPercentOfPersonsSleepin) {
        this.rankPercentOfPersonsSleepin = rankPercentOfPersonsSleepin;
    }

    /**
     * (Required)
     *
     * @return The percentPersonsWithAdequateW
     */
    public Object getPercentPersonsWithAdequateW() {
        return percentPersonsWithAdequateW;
    }

    /**
     * (Required)
     *
     * @param percentPersonsWithAdequateW The Percent_persons_with_adequate_w
     */
    public void setPercentPersonsWithAdequateW(Object percentPersonsWithAdequateW) {
        this.percentPersonsWithAdequateW = percentPersonsWithAdequateW;
    }

    /**
     * (Required)
     *
     * @return The rankPercentPersonsWithAdequ
     */
    public Object getRankPercentPersonsWithAdequ() {
        return rankPercentPersonsWithAdequ;
    }

    /**
     * (Required)
     *
     * @param rankPercentPersonsWithAdequ The Rank_Percent_persons_with_adequ
     */
    public void setRankPercentPersonsWithAdequ(Object rankPercentPersonsWithAdequ) {
        this.rankPercentPersonsWithAdequ = rankPercentPersonsWithAdequ;
    }

    /**
     * (Required)
     *
     * @return The unintentionalInjuriesTreated
     */
    public Object getUnintentionalInjuriesTreated() {
        return unintentionalInjuriesTreated;
    }

    /**
     * (Required)
     *
     * @param unintentionalInjuriesTreated The Unintentional_injuries_treated_
     */
    public void setUnintentionalInjuriesTreated(Object unintentionalInjuriesTreated) {
        this.unintentionalInjuriesTreated = unintentionalInjuriesTreated;
    }

    /**
     * (Required)
     *
     * @return The rankUnintentionalInjuriesTre
     */
    public Object getRankUnintentionalInjuriesTre() {
        return rankUnintentionalInjuriesTre;
    }

    /**
     * (Required)
     *
     * @param rankUnintentionalInjuriesTre The Rank_Unintentional_injuries_tre
     */
    public void setRankUnintentionalInjuriesTre(Object rankUnintentionalInjuriesTre) {
        this.rankUnintentionalInjuriesTre = rankUnintentionalInjuriesTre;
    }

    /**
     * (Required)
     *
     * @return The intentionalInjuriesTreatedIn
     */
    public Object getIntentionalInjuriesTreatedIn() {
        return intentionalInjuriesTreatedIn;
    }

    /**
     * (Required)
     *
     * @param intentionalInjuriesTreatedIn The Intentional_injuries_treated_in
     */
    public void setIntentionalInjuriesTreatedIn(Object intentionalInjuriesTreatedIn) {
        this.intentionalInjuriesTreatedIn = intentionalInjuriesTreatedIn;
    }

    /**
     * (Required)
     *
     * @return The rankIntentionalInjuriesTreat
     */
    public Object getRankIntentionalInjuriesTreat() {
        return rankIntentionalInjuriesTreat;
    }

    /**
     * (Required)
     *
     * @param rankIntentionalInjuriesTreat The Rank_Intentional_injuries_treat
     */
    public void setRankIntentionalInjuriesTreat(Object rankIntentionalInjuriesTreat) {
        this.rankIntentionalInjuriesTreat = rankIntentionalInjuriesTreat;
    }

    /**
     * (Required)
     *
     * @return The percentOfUnintentionalHospit
     */
    public Object getPercentOfUnintentionalHospit() {
        return percentOfUnintentionalHospit;
    }

    /**
     * (Required)
     *
     * @param percentOfUnintentionalHospit The Percent_of_unintentional_hospit
     */
    public void setPercentOfUnintentionalHospit(Object percentOfUnintentionalHospit) {
        this.percentOfUnintentionalHospit = percentOfUnintentionalHospit;
    }

    /**
     * (Required)
     *
     * @return The rankPercentOfUnintentionalH
     */
    public Object getRankPercentOfUnintentionalH() {
        return rankPercentOfUnintentionalH;
    }

    /**
     * (Required)
     *
     * @param rankPercentOfUnintentionalH The Rank_Percent_of_unintentional_h
     */
    public void setRankPercentOfUnintentionalH(Object rankPercentOfUnintentionalH) {
        this.rankPercentOfUnintentionalH = rankPercentOfUnintentionalH;
    }

    /**
     * (Required)
     *
     * @return The indirectStandardisedDeathRat
     */
    public Object getIndirectStandardisedDeathRat() {
        return indirectStandardisedDeathRat;
    }

    /**
     * (Required)
     *
     * @param indirectStandardisedDeathRat The Indirect_standardised_death_rat
     */
    public void setIndirectStandardisedDeathRat(Object indirectStandardisedDeathRat) {
        this.indirectStandardisedDeathRat = indirectStandardisedDeathRat;
    }

    /**
     * (Required)
     *
     * @return The rankIndirectStandardisedDeat
     */
    public Object getRankIndirectStandardisedDeat() {
        return rankIndirectStandardisedDeat;
    }

    /**
     * (Required)
     *
     * @param rankIndirectStandardisedDeat The Rank_Indirect_standardised_deat
     */
    public void setRankIndirectStandardisedDeat(Object rankIndirectStandardisedDeat) {
        this.rankIndirectStandardisedDeat = rankIndirectStandardisedDeat;
    }

    /**
     * (Required)
     *
     * @return The avoidableDeaths0to74YrsFor
     */
    public Object getAvoidableDeaths0to74YrsFor() {
        return avoidableDeaths0to74YrsFor;
    }

    /**
     * (Required)
     *
     * @param avoidableDeaths0to74YrsFor The Avoidable_deaths_0to74_yrs_for_
     */
    public void setAvoidableDeaths0to74YrsFor(Object avoidableDeaths0to74YrsFor) {
        this.avoidableDeaths0to74YrsFor = avoidableDeaths0to74YrsFor;
    }

    /**
     * (Required)
     *
     * @return The rankAvoidableDeaths0to74Yrs
     */
    public Object getRankAvoidableDeaths0to74Yrs() {
        return rankAvoidableDeaths0to74Yrs;
    }

    /**
     * (Required)
     *
     * @param rankAvoidableDeaths0to74Yrs The Rank_Avoidable_deaths_0to74_yrs
     */
    public void setRankAvoidableDeaths0to74Yrs(Object rankAvoidableDeaths0to74Yrs) {
        this.rankAvoidableDeaths0to74Yrs = rankAvoidableDeaths0to74Yrs;
    }

    /**
     * (Required)
     *
     * @return The avoidableDeaths0to74yrsrsFor
     */
    public Object getAvoidableDeaths0to74yrsrsFor() {
        return avoidableDeaths0to74yrsrsFor;
    }

    /**
     * (Required)
     *
     * @param avoidableDeaths0to74yrsrsFor The Avoidable_deaths_0to74yrsrs_for
     */
    public void setAvoidableDeaths0to74yrsrsFor(Object avoidableDeaths0to74yrsrsFor) {
        this.avoidableDeaths0to74yrsrsFor = avoidableDeaths0to74yrsrsFor;
    }

    /**
     * (Required)
     *
     * @return The rankAvoidableDeaths0to74yrsr
     */
    public Object getRankAvoidableDeaths0to74yrsr() {
        return rankAvoidableDeaths0to74yrsr;
    }

    /**
     * (Required)
     *
     * @param rankAvoidableDeaths0to74yrsr The Rank_Avoidable_deaths_0to74yrsr
     */
    public void setRankAvoidableDeaths0to74yrsr(Object rankAvoidableDeaths0to74yrsr) {
        this.rankAvoidableDeaths0to74yrsr = rankAvoidableDeaths0to74yrsr;
    }

    /**
     * (Required)
     *
     * @return The avoidableDeaths0to74yrsrsF1
     */
    public Object getAvoidableDeaths0to74yrsrsF1() {
        return avoidableDeaths0to74yrsrsF1;
    }

    /**
     * (Required)
     *
     * @param avoidableDeaths0to74yrsrsF1 The Avoidable_deaths_0to74yrsrs_f_1
     */
    public void setAvoidableDeaths0to74yrsrsF1(Object avoidableDeaths0to74yrsrsF1) {
        this.avoidableDeaths0to74yrsrsF1 = avoidableDeaths0to74yrsrsF1;
    }

    /**
     * (Required)
     *
     * @return The rankAvoidableDeaths0to74yr1
     */
    public Object getRankAvoidableDeaths0to74yr1() {
        return rankAvoidableDeaths0to74yr1;
    }

    /**
     * (Required)
     *
     * @param rankAvoidableDeaths0to74yr1 The Rank_Avoidable_deaths_0to74yr_1
     */
    public void setRankAvoidableDeaths0to74yr1(Object rankAvoidableDeaths0to74yr1) {
        this.rankAvoidableDeaths0to74yr1 = rankAvoidableDeaths0to74yr1;
    }

    /**
     * (Required)
     *
     * @return The avoidableDeaths0to74yrsForR
     */
    public Object getAvoidableDeaths0to74yrsForR() {
        return avoidableDeaths0to74yrsForR;
    }

    /**
     * (Required)
     *
     * @param avoidableDeaths0to74yrsForR The Avoidable_deaths_0to74yrs_for_r
     */
    public void setAvoidableDeaths0to74yrsForR(Object avoidableDeaths0to74yrsForR) {
        this.avoidableDeaths0to74yrsForR = avoidableDeaths0to74yrsForR;
    }

    /**
     * (Required)
     *
     * @return The rankAvoidableDeaths0to74yrs
     */
    public Object getRankAvoidableDeaths0to74yrs() {
        return rankAvoidableDeaths0to74yrs;
    }

    /**
     * (Required)
     *
     * @param rankAvoidableDeaths0to74yrs The Rank_Avoidable_deaths_0to74yrs_
     */
    public void setRankAvoidableDeaths0to74yrs(Object rankAvoidableDeaths0to74yrs) {
        this.rankAvoidableDeaths0to74yrs = rankAvoidableDeaths0to74yrs;
    }

    /**
     * (Required)
     *
     * @return The primaryHealthNetwork
     */
    public Object getPrimaryHealthNetwork() {
        return primaryHealthNetwork;
    }

    /**
     * (Required)
     *
     * @param primaryHealthNetwork The Primary_Health_Network
     */
    public void setPrimaryHealthNetwork(Object primaryHealthNetwork) {
        this.primaryHealthNetwork = primaryHealthNetwork;
    }

    /**
     * (Required)
     *
     * @return The primaryCarePartnership
     */
    public Object getPrimaryCarePartnership() {
        return primaryCarePartnership;
    }

    /**
     * (Required)
     *
     * @param primaryCarePartnership The Primary_Care_Partnership
     */
    public void setPrimaryCarePartnership(Object primaryCarePartnership) {
        this.primaryCarePartnership = primaryCarePartnership;
    }

    /**
     * (Required)
     *
     * @return The numberOfHospitalsAndHealth
     */
    public Object getNumberOfHospitalsAndHealth() {
        return numberOfHospitalsAndHealth;
    }

    /**
     * (Required)
     *
     * @param numberOfHospitalsAndHealth The Number_of_hospitals_and_health_
     */
    public void setNumberOfHospitalsAndHealth(Object numberOfHospitalsAndHealth) {
        this.numberOfHospitalsAndHealth = numberOfHospitalsAndHealth;
    }

    /**
     * (Required)
     *
     * @return The numberOfPublicHospitalsAnd
     */
    public Object getNumberOfPublicHospitalsAnd() {
        return numberOfPublicHospitalsAnd;
    }

    /**
     * (Required)
     *
     * @param numberOfPublicHospitalsAnd The Number_of_public_hospitals_and_
     */
    public void setNumberOfPublicHospitalsAnd(Object numberOfPublicHospitalsAnd) {
        this.numberOfPublicHospitalsAnd = numberOfPublicHospitalsAnd;
    }

    /**
     * (Required)
     *
     * @return The numberOfPrivateHospitalsAnd
     */
    public Object getNumberOfPrivateHospitalsAnd() {
        return numberOfPrivateHospitalsAnd;
    }

    /**
     * (Required)
     *
     * @param numberOfPrivateHospitalsAnd The Number_of_private_hospitals_and
     */
    public void setNumberOfPrivateHospitalsAnd(Object numberOfPrivateHospitalsAnd) {
        this.numberOfPrivateHospitalsAnd = numberOfPrivateHospitalsAnd;
    }

    /**
     * (Required)
     *
     * @return The gPsPer1000Pop
     */
    public Object getGPsPer1000Pop() {
        return gPsPer1000Pop;
    }

    /**
     * (Required)
     *
     * @param gPsPer1000Pop The GPs_per_1000_pop
     */
    public void setGPsPer1000Pop(Object gPsPer1000Pop) {
        this.gPsPer1000Pop = gPsPer1000Pop;
    }

    /**
     * (Required)
     *
     * @return The rankGPsPer1000Pop
     */
    public Object getRankGPsPer1000Pop() {
        return rankGPsPer1000Pop;
    }

    /**
     * (Required)
     *
     * @param rankGPsPer1000Pop The Rank_GPs_per_1000_pop
     */
    public void setRankGPsPer1000Pop(Object rankGPsPer1000Pop) {
        this.rankGPsPer1000Pop = rankGPsPer1000Pop;
    }

    /**
     * (Required)
     *
     * @return The gPSitesPer1000Pop
     */
    public Object getGPSitesPer1000Pop() {
        return gPSitesPer1000Pop;
    }

    /**
     * (Required)
     *
     * @param gPSitesPer1000Pop The GP_sites_per_1000_pop
     */
    public void setGPSitesPer1000Pop(Object gPSitesPer1000Pop) {
        this.gPSitesPer1000Pop = gPSitesPer1000Pop;
    }

    /**
     * (Required)
     *
     * @return The rankGPSitesPer1000Pop
     */
    public Object getRankGPSitesPer1000Pop() {
        return rankGPSitesPer1000Pop;
    }

    /**
     * (Required)
     *
     * @param rankGPSitesPer1000Pop The Rank_GP_sites_per_1000_pop
     */
    public void setRankGPSitesPer1000Pop(Object rankGPSitesPer1000Pop) {
        this.rankGPSitesPer1000Pop = rankGPSitesPer1000Pop;
    }

    /**
     * (Required)
     *
     * @return The alliedHealthSitesPer1000Po
     */
    public Object getAlliedHealthSitesPer1000Po() {
        return alliedHealthSitesPer1000Po;
    }

    /**
     * (Required)
     *
     * @param alliedHealthSitesPer1000Po The Allied_health_sites_per_1000_po
     */
    public void setAlliedHealthSitesPer1000Po(Object alliedHealthSitesPer1000Po) {
        this.alliedHealthSitesPer1000Po = alliedHealthSitesPer1000Po;
    }

    /**
     * (Required)
     *
     * @return The rankAlliedHealthSitesPer10
     */
    public Object getRankAlliedHealthSitesPer10() {
        return rankAlliedHealthSitesPer10;
    }

    /**
     * (Required)
     *
     * @param rankAlliedHealthSitesPer10 The Rank_Allied_health_sites_per_10
     */
    public void setRankAlliedHealthSitesPer10(Object rankAlliedHealthSitesPer10) {
        this.rankAlliedHealthSitesPer10 = rankAlliedHealthSitesPer10;
    }

    /**
     * (Required)
     *
     * @return The dentalServicesPer1000Pop
     */
    public Object getDentalServicesPer1000Pop() {
        return dentalServicesPer1000Pop;
    }

    /**
     * (Required)
     *
     * @param dentalServicesPer1000Pop The Dental_services_per_1000_pop
     */
    public void setDentalServicesPer1000Pop(Object dentalServicesPer1000Pop) {
        this.dentalServicesPer1000Pop = dentalServicesPer1000Pop;
    }

    /**
     * (Required)
     *
     * @return The rankDentalServicesPer1000P
     */
    public Object getRankDentalServicesPer1000P() {
        return rankDentalServicesPer1000P;
    }

    /**
     * (Required)
     *
     * @param rankDentalServicesPer1000P The Rank_Dental_services_per_1000_p
     */
    public void setRankDentalServicesPer1000P(Object rankDentalServicesPer1000P) {
        this.rankDentalServicesPer1000P = rankDentalServicesPer1000P;
    }

    /**
     * (Required)
     *
     * @return The pharmaciesPer1000Pop
     */
    public Object getPharmaciesPer1000Pop() {
        return pharmaciesPer1000Pop;
    }

    /**
     * (Required)
     *
     * @param pharmaciesPer1000Pop The Pharmacies_per_1000_pop
     */
    public void setPharmaciesPer1000Pop(Object pharmaciesPer1000Pop) {
        this.pharmaciesPer1000Pop = pharmaciesPer1000Pop;
    }

    /**
     * (Required)
     *
     * @return The rankPharmaciesPer1000Pop
     */
    public Object getRankPharmaciesPer1000Pop() {
        return rankPharmaciesPer1000Pop;
    }

    /**
     * (Required)
     *
     * @param rankPharmaciesPer1000Pop The Rank_Pharmacies_per_1000_pop
     */
    public void setRankPharmaciesPer1000Pop(Object rankPharmaciesPer1000Pop) {
        this.rankPharmaciesPer1000Pop = rankPharmaciesPer1000Pop;
    }

    /**
     * (Required)
     *
     * @return The percentNearPublicTransport
     */
    public Object getPercentNearPublicTransport() {
        return percentNearPublicTransport;
    }

    /**
     * (Required)
     *
     * @param percentNearPublicTransport The Percent_Near_Public_Transport
     */
    public void setPercentNearPublicTransport(Object percentNearPublicTransport) {
        this.percentNearPublicTransport = percentNearPublicTransport;
    }

    /**
     * (Required)
     *
     * @return The rankPercentNearPublicTransp
     */
    public Object getRankPercentNearPublicTransp() {
        return rankPercentNearPublicTransp;
    }

    /**
     * (Required)
     *
     * @param rankPercentNearPublicTransp The Rank_Percent_Near_Public_Transp
     */
    public void setRankPercentNearPublicTransp(Object rankPercentNearPublicTransp) {
        this.rankPercentNearPublicTransp = rankPercentNearPublicTransp;
    }

    /**
     * (Required)
     *
     * @return The percentWithPrivateHealthIns
     */
    public Object getPercentWithPrivateHealthIns() {
        return percentWithPrivateHealthIns;
    }

    /**
     * (Required)
     *
     * @param percentWithPrivateHealthIns The Percent_with_private_health_ins
     */
    public void setPercentWithPrivateHealthIns(Object percentWithPrivateHealthIns) {
        this.percentWithPrivateHealthIns = percentWithPrivateHealthIns;
    }

    /**
     * (Required)
     *
     * @return The rankPercentWithPrivateHealt
     */
    public Object getRankPercentWithPrivateHealt() {
        return rankPercentWithPrivateHealt;
    }

    /**
     * (Required)
     *
     * @param rankPercentWithPrivateHealt The Rank_Percent_with_private_healt
     */
    public void setRankPercentWithPrivateHealt(Object rankPercentWithPrivateHealt) {
        this.rankPercentWithPrivateHealt = rankPercentWithPrivateHealt;
    }

    /**
     * (Required)
     *
     * @return The hospitalInpatientSeparations
     */
    public Object getHospitalInpatientSeparations() {
        return hospitalInpatientSeparations;
    }

    /**
     * (Required)
     *
     * @param hospitalInpatientSeparations The Hospital_inpatient_separations_
     */
    public void setHospitalInpatientSeparations(Object hospitalInpatientSeparations) {
        this.hospitalInpatientSeparations = hospitalInpatientSeparations;
    }

    /**
     * (Required)
     *
     * @return The rankHospitalInpatientSeparat
     */
    public Object getRankHospitalInpatientSeparat() {
        return rankHospitalInpatientSeparat;
    }

    /**
     * (Required)
     *
     * @param rankHospitalInpatientSeparat The Rank_Hospital_inpatient_separat
     */
    public void setRankHospitalInpatientSeparat(Object rankHospitalInpatientSeparat) {
        this.rankHospitalInpatientSeparat = rankHospitalInpatientSeparat;
    }

    /**
     * (Required)
     *
     * @return The percentInpatientSeparationsF
     */
    public Object getPercentInpatientSeparationsF() {
        return percentInpatientSeparationsF;
    }

    /**
     * (Required)
     *
     * @param percentInpatientSeparationsF The Percent_inpatient_separations_f
     */
    public void setPercentInpatientSeparationsF(Object percentInpatientSeparationsF) {
        this.percentInpatientSeparationsF = percentInpatientSeparationsF;
    }

    /**
     * (Required)
     *
     * @return The rankPercentInpatientSeparati
     */
    public Object getRankPercentInpatientSeparati() {
        return rankPercentInpatientSeparati;
    }

    /**
     * (Required)
     *
     * @param rankPercentInpatientSeparati The Rank_Percent_inpatient_separati
     */
    public void setRankPercentInpatientSeparati(Object rankPercentInpatientSeparati) {
        this.rankPercentInpatientSeparati = rankPercentInpatientSeparati;
    }

    /**
     * (Required)
     *
     * @return The mainPublicHospitalAttendedF
     */
    public Object getMainPublicHospitalAttendedF() {
        return mainPublicHospitalAttendedF;
    }

    /**
     * (Required)
     *
     * @param mainPublicHospitalAttendedF The Main_public_hospital_attended_f
     */
    public void setMainPublicHospitalAttendedF(Object mainPublicHospitalAttendedF) {
        this.mainPublicHospitalAttendedF = mainPublicHospitalAttendedF;
    }

    /**
     * (Required)
     *
     * @return The mainPublicHospitalAttendedP
     */
    public Object getMainPublicHospitalAttendedP() {
        return mainPublicHospitalAttendedP;
    }

    /**
     * (Required)
     *
     * @param mainPublicHospitalAttendedP The Main_public_hospital_attended_P
     */
    public void setMainPublicHospitalAttendedP(Object mainPublicHospitalAttendedP) {
        this.mainPublicHospitalAttendedP = mainPublicHospitalAttendedP;
    }

    /**
     * (Required)
     *
     * @return The rankMainPublicHospitalAtten
     */
    public Object getRankMainPublicHospitalAtten() {
        return rankMainPublicHospitalAtten;
    }

    /**
     * (Required)
     *
     * @param rankMainPublicHospitalAtten The Rank_Main_public_hospital_atten
     */
    public void setRankMainPublicHospitalAtten(Object rankMainPublicHospitalAtten) {
        this.rankMainPublicHospitalAtten = rankMainPublicHospitalAtten;
    }

    /**
     * (Required)
     *
     * @return The averageLengthOfStayInDays
     */
    public Object getAverageLengthOfStayInDays() {
        return averageLengthOfStayInDays;
    }

    /**
     * (Required)
     *
     * @param averageLengthOfStayInDays The Average_length_of_stay_in_days_
     */
    public void setAverageLengthOfStayInDays(Object averageLengthOfStayInDays) {
        this.averageLengthOfStayInDays = averageLengthOfStayInDays;
    }

    /**
     * (Required)
     *
     * @return The rankAverageLengthOfStayIn
     */
    public Object getRankAverageLengthOfStayIn() {
        return rankAverageLengthOfStayIn;
    }

    /**
     * (Required)
     *
     * @param rankAverageLengthOfStayIn The Rank_Average_length_of_stay_in_
     */
    public void setRankAverageLengthOfStayIn(Object rankAverageLengthOfStayIn) {
        this.rankAverageLengthOfStayIn = rankAverageLengthOfStayIn;
    }

    /**
     * (Required)
     *
     * @return The averageLengthOfStayForAll
     */
    public Object getAverageLengthOfStayForAll() {
        return averageLengthOfStayForAll;
    }

    /**
     * (Required)
     *
     * @param averageLengthOfStayForAll The Average_length_of_stay_for_all_
     */
    public void setAverageLengthOfStayForAll(Object averageLengthOfStayForAll) {
        this.averageLengthOfStayForAll = averageLengthOfStayForAll;
    }

    /**
     * (Required)
     *
     * @return The rankAverageLengthOfStayFor
     */
    public Object getRankAverageLengthOfStayFor() {
        return rankAverageLengthOfStayFor;
    }

    /**
     * (Required)
     *
     * @param rankAverageLengthOfStayFor The Rank_Average_length_of_stay_for
     */
    public void setRankAverageLengthOfStayFor(Object rankAverageLengthOfStayFor) {
        this.rankAverageLengthOfStayFor = rankAverageLengthOfStayFor;
    }

    /**
     * (Required)
     *
     * @return The perAnnumPercentChangeInSep
     */
    public Object getPerAnnumPercentChangeInSep() {
        return perAnnumPercentChangeInSep;
    }

    /**
     * (Required)
     *
     * @param perAnnumPercentChangeInSep The Per_annum_Percent_Change_in_Sep
     */
    public void setPerAnnumPercentChangeInSep(Object perAnnumPercentChangeInSep) {
        this.perAnnumPercentChangeInSep = perAnnumPercentChangeInSep;
    }

    /**
     * (Required)
     *
     * @return The rankPerAnnumPercentChangeI
     */
    public Object getRankPerAnnumPercentChangeI() {
        return rankPerAnnumPercentChangeI;
    }

    /**
     * (Required)
     *
     * @param rankPerAnnumPercentChangeI The Rank_Per_annum_Percent_Change_i
     */
    public void setRankPerAnnumPercentChangeI(Object rankPerAnnumPercentChangeI) {
        this.rankPerAnnumPercentChangeI = rankPerAnnumPercentChangeI;
    }

    /**
     * (Required)
     *
     * @return The perAnnumPercentProjectedCha
     */
    public Object getPerAnnumPercentProjectedCha() {
        return perAnnumPercentProjectedCha;
    }

    /**
     * (Required)
     *
     * @param perAnnumPercentProjectedCha The Per_annum_Percent_Projected_Cha
     */
    public void setPerAnnumPercentProjectedCha(Object perAnnumPercentProjectedCha) {
        this.perAnnumPercentProjectedCha = perAnnumPercentProjectedCha;
    }

    /**
     * (Required)
     *
     * @return The rankPerAnnumPercentProjecte
     */
    public Object getRankPerAnnumPercentProjecte() {
        return rankPerAnnumPercentProjecte;
    }

    /**
     * (Required)
     *
     * @param rankPerAnnumPercentProjecte The Rank_Per_annum_Percent_Projecte
     */
    public void setRankPerAnnumPercentProjecte(Object rankPerAnnumPercentProjecte) {
        this.rankPerAnnumPercentProjecte = rankPerAnnumPercentProjecte;
    }

    /**
     * (Required)
     *
     * @return The aCSCsPer1000PopTotal
     */
    public Object getACSCsPer1000PopTotal() {
        return aCSCsPer1000PopTotal;
    }

    /**
     * (Required)
     *
     * @param aCSCsPer1000PopTotal The ACSCs_per_1000_pop_Total
     */
    public void setACSCsPer1000PopTotal(Object aCSCsPer1000PopTotal) {
        this.aCSCsPer1000PopTotal = aCSCsPer1000PopTotal;
    }

    /**
     * (Required)
     *
     * @return The rankACSCsPer1000PopTotal
     */
    public Object getRankACSCsPer1000PopTotal() {
        return rankACSCsPer1000PopTotal;
    }

    /**
     * (Required)
     *
     * @param rankACSCsPer1000PopTotal The Rank_ACSCs_per_1000_pop_Total
     */
    public void setRankACSCsPer1000PopTotal(Object rankACSCsPer1000PopTotal) {
        this.rankACSCsPer1000PopTotal = rankACSCsPer1000PopTotal;
    }

    /**
     * (Required)
     *
     * @return The aCSCsPer1000PopAcute
     */
    public Object getACSCsPer1000PopAcute() {
        return aCSCsPer1000PopAcute;
    }

    /**
     * (Required)
     *
     * @param aCSCsPer1000PopAcute The ACSCs_per_1000_pop_Acute
     */
    public void setACSCsPer1000PopAcute(Object aCSCsPer1000PopAcute) {
        this.aCSCsPer1000PopAcute = aCSCsPer1000PopAcute;
    }

    /**
     * (Required)
     *
     * @return The rankACSCsPer1000PopAcute
     */
    public Object getRankACSCsPer1000PopAcute() {
        return rankACSCsPer1000PopAcute;
    }

    /**
     * (Required)
     *
     * @param rankACSCsPer1000PopAcute The Rank_ACSCs_per_1000_pop_Acute
     */
    public void setRankACSCsPer1000PopAcute(Object rankACSCsPer1000PopAcute) {
        this.rankACSCsPer1000PopAcute = rankACSCsPer1000PopAcute;
    }

    /**
     * (Required)
     *
     * @return The aCSCsPer1000PopChronic
     */
    public Object getACSCsPer1000PopChronic() {
        return aCSCsPer1000PopChronic;
    }

    /**
     * (Required)
     *
     * @param aCSCsPer1000PopChronic The ACSCs_per_1000_pop_Chronic
     */
    public void setACSCsPer1000PopChronic(Object aCSCsPer1000PopChronic) {
        this.aCSCsPer1000PopChronic = aCSCsPer1000PopChronic;
    }

    /**
     * (Required)
     *
     * @return The rankACSCsPer1000PopChronic
     */
    public Object getRankACSCsPer1000PopChronic() {
        return rankACSCsPer1000PopChronic;
    }

    /**
     * (Required)
     *
     * @param rankACSCsPer1000PopChronic The Rank_ACSCs_per_1000_pop_Chronic
     */
    public void setRankACSCsPer1000PopChronic(Object rankACSCsPer1000PopChronic) {
        this.rankACSCsPer1000PopChronic = rankACSCsPer1000PopChronic;
    }

    /**
     * (Required)
     *
     * @return The aCSCsPer1000PopVaccinePrev
     */
    public Object getACSCsPer1000PopVaccinePrev() {
        return aCSCsPer1000PopVaccinePrev;
    }

    /**
     * (Required)
     *
     * @param aCSCsPer1000PopVaccinePrev The ACSCs_per_1000_pop_Vaccine_prev
     */
    public void setACSCsPer1000PopVaccinePrev(Object aCSCsPer1000PopVaccinePrev) {
        this.aCSCsPer1000PopVaccinePrev = aCSCsPer1000PopVaccinePrev;
    }

    /**
     * (Required)
     *
     * @return The rankACSCsPer1000PopVaccine
     */
    public Object getRankACSCsPer1000PopVaccine() {
        return rankACSCsPer1000PopVaccine;
    }

    /**
     * (Required)
     *
     * @param rankACSCsPer1000PopVaccine The Rank_ACSCs_per_1000_pop_Vaccine
     */
    public void setRankACSCsPer1000PopVaccine(Object rankACSCsPer1000PopVaccine) {
        this.rankACSCsPer1000PopVaccine = rankACSCsPer1000PopVaccine;
    }

    /**
     * (Required)
     *
     * @return The emergencyDepartmentPresentati
     */
    public Object getEmergencyDepartmentPresentati() {
        return emergencyDepartmentPresentati;
    }

    /**
     * (Required)
     *
     * @param emergencyDepartmentPresentati The Emergency_Department_presentati
     */
    public void setEmergencyDepartmentPresentati(Object emergencyDepartmentPresentati) {
        this.emergencyDepartmentPresentati = emergencyDepartmentPresentati;
    }

    /**
     * (Required)
     *
     * @return The rankEmergencyDepartmentPrese
     */
    public Object getRankEmergencyDepartmentPrese() {
        return rankEmergencyDepartmentPrese;
    }

    /**
     * (Required)
     *
     * @param rankEmergencyDepartmentPrese The Rank_Emergency_Department_prese
     */
    public void setRankEmergencyDepartmentPrese(Object rankEmergencyDepartmentPrese) {
        this.rankEmergencyDepartmentPrese = rankEmergencyDepartmentPrese;
    }

    /**
     * (Required)
     *
     * @return The primaryCareTypePresentations
     */
    public Object getPrimaryCareTypePresentations() {
        return primaryCareTypePresentations;
    }

    /**
     * (Required)
     *
     * @param primaryCareTypePresentations The Primary_care_type_presentations
     */
    public void setPrimaryCareTypePresentations(Object primaryCareTypePresentations) {
        this.primaryCareTypePresentations = primaryCareTypePresentations;
    }

    /**
     * (Required)
     *
     * @return The rankPrimaryCareTypePresenta
     */
    public Object getRankPrimaryCareTypePresenta() {
        return rankPrimaryCareTypePresenta;
    }

    /**
     * (Required)
     *
     * @param rankPrimaryCareTypePresenta The Rank_Primary_care_type_presenta
     */
    public void setRankPrimaryCareTypePresenta(Object rankPrimaryCareTypePresenta) {
        this.rankPrimaryCareTypePresenta = rankPrimaryCareTypePresenta;
    }

    /**
     * (Required)
     *
     * @return The childProtectionInvestigations
     */
    public Object getChildProtectionInvestigations() {
        return childProtectionInvestigations;
    }

    /**
     * (Required)
     *
     * @param childProtectionInvestigations The Child_protection_investigations
     */
    public void setChildProtectionInvestigations(Object childProtectionInvestigations) {
        this.childProtectionInvestigations = childProtectionInvestigations;
    }

    /**
     * (Required)
     *
     * @return The rankChildProtectionInvestiga
     */
    public Object getRankChildProtectionInvestiga() {
        return rankChildProtectionInvestiga;
    }

    /**
     * (Required)
     *
     * @param rankChildProtectionInvestiga The Rank_Child_protection_investiga
     */
    public void setRankChildProtectionInvestiga(Object rankChildProtectionInvestiga) {
        this.rankChildProtectionInvestiga = rankChildProtectionInvestiga;
    }

    /**
     * (Required)
     *
     * @return The childProtectionSubstantiation
     */
    public Object getChildProtectionSubstantiation() {
        return childProtectionSubstantiation;
    }

    /**
     * (Required)
     *
     * @param childProtectionSubstantiation The Child_protection_substantiation
     */
    public void setChildProtectionSubstantiation(Object childProtectionSubstantiation) {
        this.childProtectionSubstantiation = childProtectionSubstantiation;
    }

    /**
     * (Required)
     *
     * @return The rankChildProtectionSubstanti
     */
    public Object getRankChildProtectionSubstanti() {
        return rankChildProtectionSubstanti;
    }

    /**
     * (Required)
     *
     * @param rankChildProtectionSubstanti The Rank_Child_protection_substanti
     */
    public void setRankChildProtectionSubstanti(Object rankChildProtectionSubstanti) {
        this.rankChildProtectionSubstanti = rankChildProtectionSubstanti;
    }

    /**
     * (Required)
     *
     * @return The numberOfChildFIRSTAssessmen
     */
    public Object getNumberOfChildFIRSTAssessmen() {
        return numberOfChildFIRSTAssessmen;
    }

    /**
     * (Required)
     *
     * @param numberOfChildFIRSTAssessmen The Number_of_Child_FIRST_assessmen
     */
    public void setNumberOfChildFIRSTAssessmen(Object numberOfChildFIRSTAssessmen) {
        this.numberOfChildFIRSTAssessmen = numberOfChildFIRSTAssessmen;
    }

    /**
     * (Required)
     *
     * @return The rankNumberOfChildFIRSTAsse
     */
    public Object getRankNumberOfChildFIRSTAsse() {
        return rankNumberOfChildFIRSTAsse;
    }

    /**
     * (Required)
     *
     * @param rankNumberOfChildFIRSTAsse The Rank_Number_of_Child_FIRST_asse
     */
    public void setRankNumberOfChildFIRSTAsse(Object rankNumberOfChildFIRSTAsse) {
        this.rankNumberOfChildFIRSTAsse = rankNumberOfChildFIRSTAsse;
    }

    /**
     * (Required)
     *
     * @return The gPAttendancesPer1000PopMal
     */
    public Object getGPAttendancesPer1000PopMal() {
        return gPAttendancesPer1000PopMal;
    }

    /**
     * (Required)
     *
     * @param gPAttendancesPer1000PopMal The GP_attendances_per_1000_pop_Mal
     */
    public void setGPAttendancesPer1000PopMal(Object gPAttendancesPer1000PopMal) {
        this.gPAttendancesPer1000PopMal = gPAttendancesPer1000PopMal;
    }

    /**
     * (Required)
     *
     * @return The rankGPAttendancesPer1000Po
     */
    public Object getRankGPAttendancesPer1000Po() {
        return rankGPAttendancesPer1000Po;
    }

    /**
     * (Required)
     *
     * @param rankGPAttendancesPer1000Po The Rank_GP_attendances_per_1000_po
     */
    public void setRankGPAttendancesPer1000Po(Object rankGPAttendancesPer1000Po) {
        this.rankGPAttendancesPer1000Po = rankGPAttendancesPer1000Po;
    }

    /**
     * (Required)
     *
     * @return The gPAttendancesPer1000PopFem
     */
    public Object getGPAttendancesPer1000PopFem() {
        return gPAttendancesPer1000PopFem;
    }

    /**
     * (Required)
     *
     * @param gPAttendancesPer1000PopFem The GP_attendances_per_1000_pop_Fem
     */
    public void setGPAttendancesPer1000PopFem(Object gPAttendancesPer1000PopFem) {
        this.gPAttendancesPer1000PopFem = gPAttendancesPer1000PopFem;
    }

    /**
     * (Required)
     *
     * @return The rankGPAttendancesPer10001
     */
    public Object getRankGPAttendancesPer10001() {
        return rankGPAttendancesPer10001;
    }

    /**
     * (Required)
     *
     * @param rankGPAttendancesPer10001 The Rank_GP_attendances_per_1000__1
     */
    public void setRankGPAttendancesPer10001(Object rankGPAttendancesPer10001) {
        this.rankGPAttendancesPer10001 = rankGPAttendancesPer10001;
    }

    /**
     * (Required)
     *
     * @return The gPAttendancesPer1000PopTot
     */
    public Object getGPAttendancesPer1000PopTot() {
        return gPAttendancesPer1000PopTot;
    }

    /**
     * (Required)
     *
     * @param gPAttendancesPer1000PopTot The GP_attendances_per_1000_pop_Tot
     */
    public void setGPAttendancesPer1000PopTot(Object gPAttendancesPer1000PopTot) {
        this.gPAttendancesPer1000PopTot = gPAttendancesPer1000PopTot;
    }

    /**
     * (Required)
     *
     * @return The rankGPAttendancesPer10002
     */
    public Object getRankGPAttendancesPer10002() {
        return rankGPAttendancesPer10002;
    }

    /**
     * (Required)
     *
     * @param rankGPAttendancesPer10002 The Rank_GP_attendances_per_1000__2
     */
    public void setRankGPAttendancesPer10002(Object rankGPAttendancesPer10002) {
        this.rankGPAttendancesPer10002 = rankGPAttendancesPer10002;
    }

    /**
     * (Required)
     *
     * @return The hACCClientsAged0to64yrsPer
     */
    public Object getHACCClientsAged0to64yrsPer() {
        return hACCClientsAged0to64yrsPer;
    }

    /**
     * (Required)
     *
     * @param hACCClientsAged0to64yrsPer The HACC_clients_aged_0to64yrs_per_
     */
    public void setHACCClientsAged0to64yrsPer(Object hACCClientsAged0to64yrsPer) {
        this.hACCClientsAged0to64yrsPer = hACCClientsAged0to64yrsPer;
    }

    /**
     * (Required)
     *
     * @return The rankHACCClientsAged0to64yrs
     */
    public Object getRankHACCClientsAged0to64yrs() {
        return rankHACCClientsAged0to64yrs;
    }

    /**
     * (Required)
     *
     * @param rankHACCClientsAged0to64yrs The Rank_HACC_clients_aged_0to64yrs
     */
    public void setRankHACCClientsAged0to64yrs(Object rankHACCClientsAged0to64yrs) {
        this.rankHACCClientsAged0to64yrs = rankHACCClientsAged0to64yrs;
    }

    /**
     * (Required)
     *
     * @return The hACCClientsAged65yrsPlusPer
     */
    public Object getHACCClientsAged65yrsPlusPer() {
        return hACCClientsAged65yrsPlusPer;
    }

    /**
     * (Required)
     *
     * @param hACCClientsAged65yrsPlusPer The HACC_clients_aged_65yrsPlus_per
     */
    public void setHACCClientsAged65yrsPlusPer(Object hACCClientsAged65yrsPlusPer) {
        this.hACCClientsAged65yrsPlusPer = hACCClientsAged65yrsPlusPer;
    }

    /**
     * (Required)
     *
     * @return The rankHACCClientsAged65yrsPlu
     */
    public Object getRankHACCClientsAged65yrsPlu() {
        return rankHACCClientsAged65yrsPlu;
    }

    /**
     * (Required)
     *
     * @param rankHACCClientsAged65yrsPlu The Rank_HACC_clients_aged_65yrsPlu
     */
    public void setRankHACCClientsAged65yrsPlu(Object rankHACCClientsAged65yrsPlu) {
        this.rankHACCClientsAged65yrsPlu = rankHACCClientsAged65yrsPlu;
    }

    /**
     * (Required)
     *
     * @return The noClientsWhoReceivedAlcohol
     */
    public Object getNoClientsWhoReceivedAlcohol() {
        return noClientsWhoReceivedAlcohol;
    }

    /**
     * (Required)
     *
     * @param noClientsWhoReceivedAlcohol The No_clients_who_received_Alcohol
     */
    public void setNoClientsWhoReceivedAlcohol(Object noClientsWhoReceivedAlcohol) {
        this.noClientsWhoReceivedAlcohol = noClientsWhoReceivedAlcohol;
    }

    /**
     * (Required)
     *
     * @return The rankNoClientsWhoReceivedAl
     */
    public Object getRankNoClientsWhoReceivedAl() {
        return rankNoClientsWhoReceivedAl;
    }

    /**
     * (Required)
     *
     * @param rankNoClientsWhoReceivedAl The Rank_No_clients_who_received_Al
     */
    public void setRankNoClientsWhoReceivedAl(Object rankNoClientsWhoReceivedAl) {
        this.rankNoClientsWhoReceivedAl = rankNoClientsWhoReceivedAl;
    }

    /**
     * (Required)
     *
     * @return The registeredMentalMealthClient
     */
    public Object getRegisteredMentalMealthClient() {
        return registeredMentalMealthClient;
    }

    /**
     * (Required)
     *
     * @param registeredMentalMealthClient The Registered_mental_mealth_client
     */
    public void setRegisteredMentalMealthClient(Object registeredMentalMealthClient) {
        this.registeredMentalMealthClient = registeredMentalMealthClient;
    }

    /**
     * (Required)
     *
     * @return The rankRegisteredMentalMealthC
     */
    public Object getRankRegisteredMentalMealthC() {
        return rankRegisteredMentalMealthC;
    }

    /**
     * (Required)
     *
     * @param rankRegisteredMentalMealthC The Rank_Registered_mental_mealth_c
     */
    public void setRankRegisteredMentalMealthC(Object rankRegisteredMentalMealthC) {
        this.rankRegisteredMentalMealthC = rankRegisteredMentalMealthC;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(lGA)
                .append(metropolitanRural)
                .append(departmentalRegion)
                .append(departmentalArea)
                .append(areaOfLGASqKm)
                .append(aSGSLGACode)
                .append(mostPopulousTownOrSuburbIn)
                .append(distanceToMelbourneInKm)
                .append(travelTimeFromMelbourneGPO)
                .append(aRIARemotenessCategory)
                .append(percentBusinessLandUse)
                .append(percentIndustrialLandUse)
                .append(percentResidentialLandUse)
                .append(percentRuralLandUse)
                .append(percentOtherLandUse)
                .append(perAnnumPopChangeActualFor)
                .append(perAnnumPopChangeProjected)
                .append(females0to14yrs)
                .append(females15to24yrs)
                .append(females25to44yrs)
                .append(females45to64yrs)
                .append(females65to84yrs)
                .append(female85yrsPlus)
                .append(totalFemales)
                .append(males0to14yrs)
                .append(males15to24yrs)
                .append(males25to44yrs)
                .append(males45to64yrs)
                .append(males65to84yrs)
                .append(male85yrsPlus)
                .append(totalMales)
                .append(total0to14yrs)
                .append(tota15to24yrs)
                .append(tota25to44yrs)
                .append(tota45to64yrs)
                .append(tota65to84yrs)
                .append(tota85yrsPlus)
                .append(total2013ERP)
                .append(percentTotal0to14yrs)
                .append(percentTotal15to24yrs)
                .append(percentTotal25to44yrs)
                .append(percentTotal45to64yrs)
                .append(percentTotal65to84yrs)
                .append(percentTotal84yrsPlus)
                .append(totalFertilityRate2012)
                .append(rankTotalFertilityRate2012)
                .append(teenageFertilityRate2012)
                .append(rankTeenageFertilityRate201)
                .append(percentAboriginalOrTorresSt)
                .append(rankAboriginalOrTorresStrai)
                .append(percentBornOverseas2011)
                .append(rankPercentBornOverseas2011)
                .append(bornInANonEnglishSpeaking)
                .append(rankBornInANonEnglishSpea)
                .append(country1PercentForTop5Ove)
                .append(country1ForTop5OverseasCo)
                .append(country2PercentForTop5Ove)
                .append(country2ForTop5OverseasCo)
                .append(country3PercentForTop5Ove)
                .append(country3ForTop5OverseasCo)
                .append(country4PercentForTop5Ove)
                .append(country4ForTop5OverseasCo)
                .append(country5PercentForTop5Ove)
                .append(country5ForTop5OverseasCo)
                .append(percentSpeaksLOTEAtHome)
                .append(rankPercentSpeaksLOTEAtHom)
                .append(country1PercentTop5Languag)
                .append(country1Top5LanguagesSpoke)
                .append(country2PercentTop5Languag)
                .append(country2Top5LanguagesSpoke)
                .append(country3PercentTop5Languag)
                .append(country3Top5LanguagesSpoke)
                .append(country4PercentTop5Languag)
                .append(country4Top5LanguagesSpoke)
                .append(country5PercentTop5Languag)
                .append(country5Top5LanguagesSpoke)
                .append(percentLowEnglishProficiency)
                .append(rankPercentLowEnglishProfic)
                .append(ancestry1PercentTop5Ancest)
                .append(ancestry1Top5Ancestries)
                .append(ancestry2PercentTop5Ancest)
                .append(ancestry2Top5Ancestries)
                .append(ancestry3PercentTop5Ancest)
                .append(ancestry3Top5Ancestries)
                .append(ancestry4PercentTop5Ancest)
                .append(ancestry4Top5Ancestries)
                .append(ancestry5PercentTop5Ancest)
                .append(ancestry5Top5Ancestries)
                .append(newSettlerArrivalsPer100000)
                .append(rankNewSettlerArrivalsPer1)
                .append(humanitarianArrivalsAsAPerc)
                .append(rankHumanitarianArrivalsAsA)
                .append(communityAcceptanceOfDiverse)
                .append(rankCommunityAcceptanceOfDi)
                .append(proportionOfHouseholdsWithB)
                .append(householdsWithBroadbandInter)
                .append(gamingMachineLossesPerHead)
                .append(rankGamingMachineLossesPer)
                .append(familyIncidentsPer1000Pop)
                .append(rankFamilyIncidentsPer1000)
                .append(drugUsageAndPossessionOffen)
                .append(rankDrugUsageAndPossession)
                .append(totalCrimePer1000Pop)
                .append(rankTotalCrimePer1000Pop)
                .append(feelsSafeWalkingAloneDuring)
                .append(rankFeelsSafeWalkingAloneD)
                .append(believeOtherPeopleCanBeTru)
                .append(rankBelieveOtherPeopleCanB)
                .append(spokeWithMoreThan5PeopleT)
                .append(rankSpokeWithMoreThan5Peo)
                .append(ableToDefinitelyGetHelpFro)
                .append(rankAbleToDefinitelyGetHel)
                .append(volunteers)
                .append(rankVolunteers)
                .append(feelValuedBySociety)
                .append(rankFeelValuedBySociety)
                .append(attendedALocalCommunityEven)
                .append(rankAttendedALocalCommunity)
                .append(takeActionOnBehalfOfTheLo)
                .append(rankTakeActionOnBehalfOfT)
                .append(membersOfASportsGroup)
                .append(rankMembersOfASportsGroup)
                .append(membersOfAReligiousGroup)
                .append(rankMembersOfAReligiousGro)
                .append(ratedTheirCommunityAsAnAct)
                .append(rankRatedTheirCommunityAsA)
                .append(ratedTheirCommunityAsAPlea)
                .append(rankAtedTheirCommunityAsA)
                .append(ratedTheirCommunityAsGoodO)
                .append(rankRatedTheirCommunityAsG)
                .append(indexOfRelativeSociaEconomi)
                .append(rankIndexOfRelativeSociaEc)
                .append(unemploymentRate)
                .append(rankUnemploymentRate)
                .append(percentIndividualIncomeLess)
                .append(rankPercentIndividualIncome)
                .append(percentFemaleIncomeLessThan)
                .append(rankPercentFemaleIncomeLess)
                .append(percentMaleIncomeLessThan4)
                .append(rankPercentMaleIncomeLessT)
                .append(percentOneParentHeadedFamil)
                .append(rankPercentOneParentHeaded)
                .append(oneParentHeadedFamiliesPerc)
                .append(rankOneParentHeadedFamilies)
                .append(oneParentHeadedFamiliesPe1)
                .append(rankOneParentHeadedFamili1)
                .append(equivalisedMedianIncome)
                .append(rankEquivalisedMedianIncome)
                .append(delayedMedicalConsultationBe)
                .append(rankDelayedMedicalConsultati)
                .append(delayedPurchasingPrescribedM)
                .append(rankDelayedPurchasingPrescri)
                .append(percentLowIncomeWelfareDepe)
                .append(rankPercentLowIncomeWelfare)
                .append(percentOfPopWithFoodInsecu)
                .append(rankPercentOfPopWithFoodI)
                .append(percentMortgageStress)
                .append(rankPercentMortgageStress)
                .append(percentRentalStress)
                .append(rankPercentRentalStress)
                .append(percentOfRentalHousingThat)
                .append(rankPercentOfRentalHousing)
                .append(medianHousePrice)
                .append(rankMedianHousePrice)
                .append(medianRentFor3BedroomsHome)
                .append(rankMedianRentFor3Bedrooms)
                .append(newDwellingsApprovedForCons)
                .append(rankNewDwellingsApprovedFor)
                .append(socialHousingStockAsAPerce)
                .append(rankSocialHousingStockAsA)
                .append(numberOfSocialHousingDwelli)
                .append(rankNumberOfSocialHousingD)
                .append(homelessnessRatePer1000Pop)
                .append(rankHomelessnessRatePer1000)
                .append(percentOfWorkJourneysWhich)
                .append(rankPercentOfWorkJourneysW)
                .append(percentOfWorkJourneysWhich1)
                .append(rankPercentOfWorkJourneys1)
                .append(personsWithAtLeast2HourDa)
                .append(rankPersonsWithAtLeast2Ho)
                .append(percentHouseholdsWithNoMoto)
                .append(rankPercentHouseholdsWithNo)
                .append(fTEStudents)
                .append(percentYear9StudentsWhoAtt)
                .append(rankPercentOfYear9Students)
                .append(percentOfYear9StudentsWho)
                .append(rankPercentYear9StudentsWh)
                .append(percent19YearOldsCompleting)
                .append(rankPercent19YearOldsCompl)
                .append(percentPersonsWhoDidNotCom)
                .append(rankPercentOfPersonsWhoDid)
                .append(percentPersonsWhoCompletedA)
                .append(rankPercentPersonsWhoComple)
                .append(percentOfSchoolChildrenAtte)
                .append(rankPercentOfSchoolChildren)
                .append(percentOfPersonsReportingAs)
                .append(rankPercentOfPersonsReporti)
                .append(percentOfPersonsReportingTy)
                .append(rankPercentOfPersonsRepor1)
                .append(percentOfPersonsReportingHi)
                .append(rankPercentOfPersonsRepor2)
                .append(percentOfPersonsReportingHe)
                .append(rankPercentOfPersonsRepor3)
                .append(percentOfPersonsReportingOs)
                .append(rankPercentOfPersonsRepor4)
                .append(percentOfPersonsReportingAr)
                .append(rankPercentOfPersonsRepor5)
                .append(percentOfPersonsWhoAreOver)
                .append(rankPercentOfPersonsWhoAre)
                .append(percentOfFemalesWhoAreOver)
                .append(rankPercentOfFemalesWhoAre)
                .append(percentOfMalesWhoAreOverwe)
                .append(rankPercentOfMalesWhoAreO)
                .append(percentOfPersonsWhoAreObes)
                .append(rankPercentOfPersonsWhoA1)
                .append(percentOfFemalesWhoAreObes)
                .append(rankPercentOfFemalesWhoA1)
                .append(percentOfMalesWhoAreObese)
                .append(rankPercentOfMalesWhoAre1)
                .append(malignantCancersDiagnosedPer)
                .append(rankMalignantCancersDiagnose)
                .append(maleCancerIncidencePer1000)
                .append(rankMaleCancerIncidencePer)
                .append(femaleCancerIncidencePer100)
                .append(rankFemaleCancerIncidencePe)
                .append(percentPoorDentalHealth)
                .append(rankPercentPoorDentalHealth)
                .append(notificationsPer100000PopOf)
                .append(rankNotificationsPer100000P)
                .append(notificationsPer100000Pop1)
                .append(rankNotificationsPer1000001)
                .append(notificationsPer100000People)
                .append(rankNotificationsPer1000002)
                .append(percentOfPersons18yrsPlusWh)
                .append(rankPercentOfPersons18yrsPl)
                .append(percentOfMales18yrsPlusWho)
                .append(rankPercentOfMales18yrsPlus)
                .append(percentOfFemales18yrsPlusWh)
                .append(rankPercentOfFemales18yrsPl)
                .append(consumedAlcoholAtLeastWeekl)
                .append(rankConsumedAlcoholAtLeast)
                .append(consumedAlcoholAtLeastWee1)
                .append(rankConsumedAlcoholAtLeast1)
                .append(percentOfPersonsWhoDoNotM)
                .append(rankPercentOfPersonsWhoDo)
                .append(percentOfMalesWhoDoNotMee)
                .append(rankPercentOfMalesWhoDoNo)
                .append(percentOfFemalesWhoDoNotM)
                .append(rankPercentOfFemalesWhoDo)
                .append(percentOfPersonsWhoDrinkSo)
                .append(rankPercentOfPersonsWhoDri)
                .append(percentOfPersonsWhoShareA)
                .append(rankPercentOfPersonsWhoSha)
                .append(percentOfPersonsWhoDoNot1)
                .append(rankPercentOfPersonsWhoDo1)
                .append(percentOfMalesWhoDoNotM1)
                .append(rankPercentOfMalesWhoDo1)
                .append(percentOfFemalesWhoDoNot1)
                .append(rankPercentOfFemalesWhoDo1)
                .append(percentOfPersonsWhoSitFor)
                .append(rankPercentOfPersonsWhoSit)
                .append(percentOfPersonsWhoVisitA)
                .append(rankPercentOfPersonsWhoVis)
                .append(percentOfBreastScreeningPar)
                .append(rankPercentOfBreastScreenin)
                .append(percentOfCervicalCancerScre)
                .append(rankPercentOfCervicalCancer)
                .append(bowelCancerScreeningParticip)
                .append(rankBowelCancerScreeningPar)
                .append(bowelCancerScreeningPartic1)
                .append(rankBowelCancerScreeningP1)
                .append(bowelCancerScreeningPartic2)
                .append(rankBowelCancerScreeningP2)
                .append(lowBirthweightBabies)
                .append(rankLowBirthweightBabies)
                .append(percentInfantsFullyBreastfed)
                .append(rankPercentInfantsFullyBrea)
                .append(percentChildrenFullyImmunise)
                .append(rankPercentChildrenFullyImm)
                .append(proportionOfInfantsEnrolled)
                .append(rankProportionOfInfantsEnro)
                .append(kindergartenParticipationRate)
                .append(percentOfChildrenWithKinder)
                .append(rankPercentOfChildrenWithK)
                .append(percentOfChildrenWithEmotio)
                .append(rankPercentOfChildrenWithE)
                .append(percentOfChildrenWithSpeech)
                .append(rankPercentOfChildrenWithS)
                .append(percentOfAdolescentsWhoRepo)
                .append(rankPercentOfAdolescentsWho)
                .append(percentOfChildrenWhoAreDev)
                .append(rankPercentOfChildrenWhoAr)
                .append(percentOfChildrenWhoAreD1)
                .append(rankPercentOfChildrenWho1)
                .append(coreActivityNeedForAssistan)
                .append(rankCoreActivityNeedForAss)
                .append(peopleWithSevereAndProfound)
                .append(rankPeopleWithSevereAndPro)
                .append(peopleWithSevereAndProfou1)
                .append(rankPeopleWithSevereAndP1)
                .append(percentPopAged75yrsPlusLivi)
                .append(rankPercentPopAged75yrsPlus)
                .append(popAged75yrsPlusLivingAlone)
                .append(rankPopAged75yrsPlusLiving)
                .append(popAged75yrsPlusLivingAlo1)
                .append(rankPopAged75yrsPlusLiving1)
                .append(personsReceivingDisabilitySe)
                .append(rankPersonsReceivingDisabili)
                .append(disabilityPensionPer1000Eli)
                .append(rankDisabilityPensionPer100)
                .append(agedCareHighCareBeds)
                .append(agedCareLowCareBeds)
                .append(agePensionPer1000EligibleP)
                .append(rankAgePensionPer1000Eligi)
                .append(maleLifeExpectancy)
                .append(rankMaleLifeExpectancy)
                .append(femaleLifeExpectancy)
                .append(rankFemaleLifeExpectancy)
                .append(personsReportingFairOrPoor)
                .append(rankPersonsReportingFairOr)
                .append(femalesReportingFairOrPoor)
                .append(rankFemalesReportingFairOr)
                .append(malesReportingFairOrPoorHe)
                .append(rankMalesReportingFairOrPo)
                .append(percentWhoHaveAHighDegree)
                .append(rankPercentWhoHaveAHighDe)
                .append(percentOfPersonsSleepingLes)
                .append(rankPercentOfPersonsSleepin)
                .append(percentPersonsWithAdequateW)
                .append(rankPercentPersonsWithAdequ)
                .append(unintentionalInjuriesTreated)
                .append(rankUnintentionalInjuriesTre)
                .append(intentionalInjuriesTreatedIn)
                .append(rankIntentionalInjuriesTreat)
                .append(percentOfUnintentionalHospit)
                .append(rankPercentOfUnintentionalH)
                .append(indirectStandardisedDeathRat)
                .append(rankIndirectStandardisedDeat)
                .append(avoidableDeaths0to74YrsFor)
                .append(rankAvoidableDeaths0to74Yrs)
                .append(avoidableDeaths0to74yrsrsFor)
                .append(rankAvoidableDeaths0to74yrsr)
                .append(avoidableDeaths0to74yrsrsF1)
                .append(rankAvoidableDeaths0to74yr1)
                .append(avoidableDeaths0to74yrsForR)
                .append(rankAvoidableDeaths0to74yrs)
                .append(primaryHealthNetwork)
                .append(primaryCarePartnership)
                .append(numberOfHospitalsAndHealth)
                .append(numberOfPublicHospitalsAnd)
                .append(numberOfPrivateHospitalsAnd)
                .append(gPsPer1000Pop)
                .append(rankGPsPer1000Pop)
                .append(gPSitesPer1000Pop)
                .append(rankGPSitesPer1000Pop)
                .append(alliedHealthSitesPer1000Po)
                .append(rankAlliedHealthSitesPer10)
                .append(dentalServicesPer1000Pop)
                .append(rankDentalServicesPer1000P)
                .append(pharmaciesPer1000Pop)
                .append(rankPharmaciesPer1000Pop)
                .append(percentNearPublicTransport)
                .append(rankPercentNearPublicTransp)
                .append(percentWithPrivateHealthIns)
                .append(rankPercentWithPrivateHealt)
                .append(hospitalInpatientSeparations)
                .append(rankHospitalInpatientSeparat)
                .append(percentInpatientSeparationsF)
                .append(rankPercentInpatientSeparati)
                .append(mainPublicHospitalAttendedF)
                .append(mainPublicHospitalAttendedP)
                .append(rankMainPublicHospitalAtten)
                .append(averageLengthOfStayInDays)
                .append(rankAverageLengthOfStayIn)
                .append(averageLengthOfStayForAll)
                .append(rankAverageLengthOfStayFor)
                .append(perAnnumPercentChangeInSep)
                .append(rankPerAnnumPercentChangeI)
                .append(perAnnumPercentProjectedCha)
                .append(rankPerAnnumPercentProjecte)
                .append(aCSCsPer1000PopTotal)
                .append(rankACSCsPer1000PopTotal)
                .append(aCSCsPer1000PopAcute)
                .append(rankACSCsPer1000PopAcute)
                .append(aCSCsPer1000PopChronic)
                .append(rankACSCsPer1000PopChronic)
                .append(aCSCsPer1000PopVaccinePrev)
                .append(rankACSCsPer1000PopVaccine)
                .append(emergencyDepartmentPresentati)
                .append(rankEmergencyDepartmentPrese)
                .append(primaryCareTypePresentations)
                .append(rankPrimaryCareTypePresenta)
                .append(childProtectionInvestigations)
                .append(rankChildProtectionInvestiga)
                .append(childProtectionSubstantiation)
                .append(rankChildProtectionSubstanti)
                .append(numberOfChildFIRSTAssessmen)
                .append(rankNumberOfChildFIRSTAsse)
                .append(gPAttendancesPer1000PopMal)
                .append(rankGPAttendancesPer1000Po)
                .append(gPAttendancesPer1000PopFem)
                .append(rankGPAttendancesPer10001)
                .append(gPAttendancesPer1000PopTot)
                .append(rankGPAttendancesPer10002)
                .append(hACCClientsAged0to64yrsPer)
                .append(rankHACCClientsAged0to64yrs)
                .append(hACCClientsAged65yrsPlusPer)
                .append(rankHACCClientsAged65yrsPlu)
                .append(noClientsWhoReceivedAlcohol)
                .append(rankNoClientsWhoReceivedAl)
                .append(registeredMentalMealthClient)
                .append(rankRegisteredMentalMealthC)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Attributes) == false) {
            return false;
        }
        Attributes rhs = ((Attributes) other);
        return new EqualsBuilder()
                .append(lGA, rhs.lGA)
                .append(metropolitanRural, rhs.metropolitanRural)
                .append(departmentalRegion, rhs.departmentalRegion)
                .append(departmentalArea, rhs.departmentalArea)
                .append(areaOfLGASqKm, rhs.areaOfLGASqKm)
                .append(aSGSLGACode, rhs.aSGSLGACode)
                .append(mostPopulousTownOrSuburbIn, rhs.mostPopulousTownOrSuburbIn)
                .append(distanceToMelbourneInKm, rhs.distanceToMelbourneInKm)
                .append(travelTimeFromMelbourneGPO, rhs.travelTimeFromMelbourneGPO)
                .append(aRIARemotenessCategory, rhs.aRIARemotenessCategory)
                .append(percentBusinessLandUse, rhs.percentBusinessLandUse)
                .append(percentIndustrialLandUse, rhs.percentIndustrialLandUse)
                .append(percentResidentialLandUse, rhs.percentResidentialLandUse)
                .append(percentRuralLandUse, rhs.percentRuralLandUse)
                .append(percentOtherLandUse, rhs.percentOtherLandUse)
                .append(perAnnumPopChangeActualFor, rhs.perAnnumPopChangeActualFor)
                .append(perAnnumPopChangeProjected, rhs.perAnnumPopChangeProjected)
                .append(females0to14yrs, rhs.females0to14yrs)
                .append(females15to24yrs, rhs.females15to24yrs)
                .append(females25to44yrs, rhs.females25to44yrs)
                .append(females45to64yrs, rhs.females45to64yrs)
                .append(females65to84yrs, rhs.females65to84yrs)
                .append(female85yrsPlus, rhs.female85yrsPlus)
                .append(totalFemales, rhs.totalFemales)
                .append(males0to14yrs, rhs.males0to14yrs)
                .append(males15to24yrs, rhs.males15to24yrs)
                .append(males25to44yrs, rhs.males25to44yrs)
                .append(males45to64yrs, rhs.males45to64yrs)
                .append(males65to84yrs, rhs.males65to84yrs)
                .append(male85yrsPlus, rhs.male85yrsPlus)
                .append(totalMales, rhs.totalMales)
                .append(total0to14yrs, rhs.total0to14yrs)
                .append(tota15to24yrs, rhs.tota15to24yrs)
                .append(tota25to44yrs, rhs.tota25to44yrs)
                .append(tota45to64yrs, rhs.tota45to64yrs)
                .append(tota65to84yrs, rhs.tota65to84yrs)
                .append(tota85yrsPlus, rhs.tota85yrsPlus)
                .append(total2013ERP, rhs.total2013ERP)
                .append(percentTotal0to14yrs, rhs.percentTotal0to14yrs)
                .append(percentTotal15to24yrs, rhs.percentTotal15to24yrs)
                .append(percentTotal25to44yrs, rhs.percentTotal25to44yrs)
                .append(percentTotal45to64yrs, rhs.percentTotal45to64yrs)
                .append(percentTotal65to84yrs, rhs.percentTotal65to84yrs)
                .append(percentTotal84yrsPlus, rhs.percentTotal84yrsPlus)
                .append(totalFertilityRate2012, rhs.totalFertilityRate2012)
                .append(rankTotalFertilityRate2012, rhs.rankTotalFertilityRate2012)
                .append(teenageFertilityRate2012, rhs.teenageFertilityRate2012)
                .append(rankTeenageFertilityRate201, rhs.rankTeenageFertilityRate201)
                .append(percentAboriginalOrTorresSt, rhs.percentAboriginalOrTorresSt)
                .append(rankAboriginalOrTorresStrai, rhs.rankAboriginalOrTorresStrai)
                .append(percentBornOverseas2011, rhs.percentBornOverseas2011)
                .append(rankPercentBornOverseas2011, rhs.rankPercentBornOverseas2011)
                .append(bornInANonEnglishSpeaking, rhs.bornInANonEnglishSpeaking)
                .append(rankBornInANonEnglishSpea, rhs.rankBornInANonEnglishSpea)
                .append(country1PercentForTop5Ove, rhs.country1PercentForTop5Ove)
                .append(country1ForTop5OverseasCo, rhs.country1ForTop5OverseasCo)
                .append(country2PercentForTop5Ove, rhs.country2PercentForTop5Ove)
                .append(country2ForTop5OverseasCo, rhs.country2ForTop5OverseasCo)
                .append(country3PercentForTop5Ove, rhs.country3PercentForTop5Ove)
                .append(country3ForTop5OverseasCo, rhs.country3ForTop5OverseasCo)
                .append(country4PercentForTop5Ove, rhs.country4PercentForTop5Ove)
                .append(country4ForTop5OverseasCo, rhs.country4ForTop5OverseasCo)
                .append(country5PercentForTop5Ove, rhs.country5PercentForTop5Ove)
                .append(country5ForTop5OverseasCo, rhs.country5ForTop5OverseasCo)
                .append(percentSpeaksLOTEAtHome, rhs.percentSpeaksLOTEAtHome)
                .append(rankPercentSpeaksLOTEAtHom, rhs.rankPercentSpeaksLOTEAtHom)
                .append(country1PercentTop5Languag, rhs.country1PercentTop5Languag)
                .append(country1Top5LanguagesSpoke, rhs.country1Top5LanguagesSpoke)
                .append(country2PercentTop5Languag, rhs.country2PercentTop5Languag)
                .append(country2Top5LanguagesSpoke, rhs.country2Top5LanguagesSpoke)
                .append(country3PercentTop5Languag, rhs.country3PercentTop5Languag)
                .append(country3Top5LanguagesSpoke, rhs.country3Top5LanguagesSpoke)
                .append(country4PercentTop5Languag, rhs.country4PercentTop5Languag)
                .append(country4Top5LanguagesSpoke, rhs.country4Top5LanguagesSpoke)
                .append(country5PercentTop5Languag, rhs.country5PercentTop5Languag)
                .append(country5Top5LanguagesSpoke, rhs.country5Top5LanguagesSpoke)
                .append(percentLowEnglishProficiency, rhs.percentLowEnglishProficiency)
                .append(rankPercentLowEnglishProfic, rhs.rankPercentLowEnglishProfic)
                .append(ancestry1PercentTop5Ancest, rhs.ancestry1PercentTop5Ancest)
                .append(ancestry1Top5Ancestries, rhs.ancestry1Top5Ancestries)
                .append(ancestry2PercentTop5Ancest, rhs.ancestry2PercentTop5Ancest)
                .append(ancestry2Top5Ancestries, rhs.ancestry2Top5Ancestries)
                .append(ancestry3PercentTop5Ancest, rhs.ancestry3PercentTop5Ancest)
                .append(ancestry3Top5Ancestries, rhs.ancestry3Top5Ancestries)
                .append(ancestry4PercentTop5Ancest, rhs.ancestry4PercentTop5Ancest)
                .append(ancestry4Top5Ancestries, rhs.ancestry4Top5Ancestries)
                .append(ancestry5PercentTop5Ancest, rhs.ancestry5PercentTop5Ancest)
                .append(ancestry5Top5Ancestries, rhs.ancestry5Top5Ancestries)
                .append(newSettlerArrivalsPer100000, rhs.newSettlerArrivalsPer100000)
                .append(rankNewSettlerArrivalsPer1, rhs.rankNewSettlerArrivalsPer1)
                .append(humanitarianArrivalsAsAPerc, rhs.humanitarianArrivalsAsAPerc)
                .append(rankHumanitarianArrivalsAsA, rhs.rankHumanitarianArrivalsAsA)
                .append(communityAcceptanceOfDiverse, rhs.communityAcceptanceOfDiverse)
                .append(rankCommunityAcceptanceOfDi, rhs.rankCommunityAcceptanceOfDi)
                .append(proportionOfHouseholdsWithB, rhs.proportionOfHouseholdsWithB)
                .append(householdsWithBroadbandInter, rhs.householdsWithBroadbandInter)
                .append(gamingMachineLossesPerHead, rhs.gamingMachineLossesPerHead)
                .append(rankGamingMachineLossesPer, rhs.rankGamingMachineLossesPer)
                .append(familyIncidentsPer1000Pop, rhs.familyIncidentsPer1000Pop)
                .append(rankFamilyIncidentsPer1000, rhs.rankFamilyIncidentsPer1000)
                .append(drugUsageAndPossessionOffen, rhs.drugUsageAndPossessionOffen)
                .append(rankDrugUsageAndPossession, rhs.rankDrugUsageAndPossession)
                .append(totalCrimePer1000Pop, rhs.totalCrimePer1000Pop)
                .append(rankTotalCrimePer1000Pop, rhs.rankTotalCrimePer1000Pop)
                .append(feelsSafeWalkingAloneDuring, rhs.feelsSafeWalkingAloneDuring)
                .append(rankFeelsSafeWalkingAloneD, rhs.rankFeelsSafeWalkingAloneD)
                .append(believeOtherPeopleCanBeTru, rhs.believeOtherPeopleCanBeTru)
                .append(rankBelieveOtherPeopleCanB, rhs.rankBelieveOtherPeopleCanB)
                .append(spokeWithMoreThan5PeopleT, rhs.spokeWithMoreThan5PeopleT)
                .append(rankSpokeWithMoreThan5Peo, rhs.rankSpokeWithMoreThan5Peo)
                .append(ableToDefinitelyGetHelpFro, rhs.ableToDefinitelyGetHelpFro)
                .append(rankAbleToDefinitelyGetHel, rhs.rankAbleToDefinitelyGetHel)
                .append(volunteers, rhs.volunteers)
                .append(rankVolunteers, rhs.rankVolunteers)
                .append(feelValuedBySociety, rhs.feelValuedBySociety)
                .append(rankFeelValuedBySociety, rhs.rankFeelValuedBySociety)
                .append(attendedALocalCommunityEven, rhs.attendedALocalCommunityEven)
                .append(rankAttendedALocalCommunity, rhs.rankAttendedALocalCommunity)
                .append(takeActionOnBehalfOfTheLo, rhs.takeActionOnBehalfOfTheLo)
                .append(rankTakeActionOnBehalfOfT, rhs.rankTakeActionOnBehalfOfT)
                .append(membersOfASportsGroup, rhs.membersOfASportsGroup)
                .append(rankMembersOfASportsGroup, rhs.rankMembersOfASportsGroup)
                .append(membersOfAReligiousGroup, rhs.membersOfAReligiousGroup)
                .append(rankMembersOfAReligiousGro, rhs.rankMembersOfAReligiousGro)
                .append(ratedTheirCommunityAsAnAct, rhs.ratedTheirCommunityAsAnAct)
                .append(rankRatedTheirCommunityAsA, rhs.rankRatedTheirCommunityAsA)
                .append(ratedTheirCommunityAsAPlea, rhs.ratedTheirCommunityAsAPlea)
                .append(rankAtedTheirCommunityAsA, rhs.rankAtedTheirCommunityAsA)
                .append(ratedTheirCommunityAsGoodO, rhs.ratedTheirCommunityAsGoodO)
                .append(rankRatedTheirCommunityAsG, rhs.rankRatedTheirCommunityAsG)
                .append(indexOfRelativeSociaEconomi, rhs.indexOfRelativeSociaEconomi)
                .append(rankIndexOfRelativeSociaEc, rhs.rankIndexOfRelativeSociaEc)
                .append(unemploymentRate, rhs.unemploymentRate)
                .append(rankUnemploymentRate, rhs.rankUnemploymentRate)
                .append(percentIndividualIncomeLess, rhs.percentIndividualIncomeLess)
                .append(rankPercentIndividualIncome, rhs.rankPercentIndividualIncome)
                .append(percentFemaleIncomeLessThan, rhs.percentFemaleIncomeLessThan)
                .append(rankPercentFemaleIncomeLess, rhs.rankPercentFemaleIncomeLess)
                .append(percentMaleIncomeLessThan4, rhs.percentMaleIncomeLessThan4)
                .append(rankPercentMaleIncomeLessT, rhs.rankPercentMaleIncomeLessT)
                .append(percentOneParentHeadedFamil, rhs.percentOneParentHeadedFamil)
                .append(rankPercentOneParentHeaded, rhs.rankPercentOneParentHeaded)
                .append(oneParentHeadedFamiliesPerc, rhs.oneParentHeadedFamiliesPerc)
                .append(rankOneParentHeadedFamilies, rhs.rankOneParentHeadedFamilies)
                .append(oneParentHeadedFamiliesPe1, rhs.oneParentHeadedFamiliesPe1)
                .append(rankOneParentHeadedFamili1, rhs.rankOneParentHeadedFamili1)
                .append(equivalisedMedianIncome, rhs.equivalisedMedianIncome)
                .append(rankEquivalisedMedianIncome, rhs.rankEquivalisedMedianIncome)
                .append(delayedMedicalConsultationBe, rhs.delayedMedicalConsultationBe)
                .append(rankDelayedMedicalConsultati, rhs.rankDelayedMedicalConsultati)
                .append(delayedPurchasingPrescribedM, rhs.delayedPurchasingPrescribedM)
                .append(rankDelayedPurchasingPrescri, rhs.rankDelayedPurchasingPrescri)
                .append(percentLowIncomeWelfareDepe, rhs.percentLowIncomeWelfareDepe)
                .append(rankPercentLowIncomeWelfare, rhs.rankPercentLowIncomeWelfare)
                .append(percentOfPopWithFoodInsecu, rhs.percentOfPopWithFoodInsecu)
                .append(rankPercentOfPopWithFoodI, rhs.rankPercentOfPopWithFoodI)
                .append(percentMortgageStress, rhs.percentMortgageStress)
                .append(rankPercentMortgageStress, rhs.rankPercentMortgageStress)
                .append(percentRentalStress, rhs.percentRentalStress)
                .append(rankPercentRentalStress, rhs.rankPercentRentalStress)
                .append(percentOfRentalHousingThat, rhs.percentOfRentalHousingThat)
                .append(rankPercentOfRentalHousing, rhs.rankPercentOfRentalHousing)
                .append(medianHousePrice, rhs.medianHousePrice)
                .append(rankMedianHousePrice, rhs.rankMedianHousePrice)
                .append(medianRentFor3BedroomsHome, rhs.medianRentFor3BedroomsHome)
                .append(rankMedianRentFor3Bedrooms, rhs.rankMedianRentFor3Bedrooms)
                .append(newDwellingsApprovedForCons, rhs.newDwellingsApprovedForCons)
                .append(rankNewDwellingsApprovedFor, rhs.rankNewDwellingsApprovedFor)
                .append(socialHousingStockAsAPerce, rhs.socialHousingStockAsAPerce)
                .append(rankSocialHousingStockAsA, rhs.rankSocialHousingStockAsA)
                .append(numberOfSocialHousingDwelli, rhs.numberOfSocialHousingDwelli)
                .append(rankNumberOfSocialHousingD, rhs.rankNumberOfSocialHousingD)
                .append(homelessnessRatePer1000Pop, rhs.homelessnessRatePer1000Pop)
                .append(rankHomelessnessRatePer1000, rhs.rankHomelessnessRatePer1000)
                .append(percentOfWorkJourneysWhich, rhs.percentOfWorkJourneysWhich)
                .append(rankPercentOfWorkJourneysW, rhs.rankPercentOfWorkJourneysW)
                .append(percentOfWorkJourneysWhich1, rhs.percentOfWorkJourneysWhich1)
                .append(rankPercentOfWorkJourneys1, rhs.rankPercentOfWorkJourneys1)
                .append(personsWithAtLeast2HourDa, rhs.personsWithAtLeast2HourDa)
                .append(rankPersonsWithAtLeast2Ho, rhs.rankPersonsWithAtLeast2Ho)
                .append(percentHouseholdsWithNoMoto, rhs.percentHouseholdsWithNoMoto)
                .append(rankPercentHouseholdsWithNo, rhs.rankPercentHouseholdsWithNo)
                .append(fTEStudents, rhs.fTEStudents)
                .append(percentYear9StudentsWhoAtt, rhs.percentYear9StudentsWhoAtt)
                .append(rankPercentOfYear9Students, rhs.rankPercentOfYear9Students)
                .append(percentOfYear9StudentsWho, rhs.percentOfYear9StudentsWho)
                .append(rankPercentYear9StudentsWh, rhs.rankPercentYear9StudentsWh)
                .append(percent19YearOldsCompleting, rhs.percent19YearOldsCompleting)
                .append(rankPercent19YearOldsCompl, rhs.rankPercent19YearOldsCompl)
                .append(percentPersonsWhoDidNotCom, rhs.percentPersonsWhoDidNotCom)
                .append(rankPercentOfPersonsWhoDid, rhs.rankPercentOfPersonsWhoDid)
                .append(percentPersonsWhoCompletedA, rhs.percentPersonsWhoCompletedA)
                .append(rankPercentPersonsWhoComple, rhs.rankPercentPersonsWhoComple)
                .append(percentOfSchoolChildrenAtte, rhs.percentOfSchoolChildrenAtte)
                .append(rankPercentOfSchoolChildren, rhs.rankPercentOfSchoolChildren)
                .append(percentOfPersonsReportingAs, rhs.percentOfPersonsReportingAs)
                .append(rankPercentOfPersonsReporti, rhs.rankPercentOfPersonsReporti)
                .append(percentOfPersonsReportingTy, rhs.percentOfPersonsReportingTy)
                .append(rankPercentOfPersonsRepor1, rhs.rankPercentOfPersonsRepor1)
                .append(percentOfPersonsReportingHi, rhs.percentOfPersonsReportingHi)
                .append(rankPercentOfPersonsRepor2, rhs.rankPercentOfPersonsRepor2)
                .append(percentOfPersonsReportingHe, rhs.percentOfPersonsReportingHe)
                .append(rankPercentOfPersonsRepor3, rhs.rankPercentOfPersonsRepor3)
                .append(percentOfPersonsReportingOs, rhs.percentOfPersonsReportingOs)
                .append(rankPercentOfPersonsRepor4, rhs.rankPercentOfPersonsRepor4)
                .append(percentOfPersonsReportingAr, rhs.percentOfPersonsReportingAr)
                .append(rankPercentOfPersonsRepor5, rhs.rankPercentOfPersonsRepor5)
                .append(percentOfPersonsWhoAreOver, rhs.percentOfPersonsWhoAreOver)
                .append(rankPercentOfPersonsWhoAre, rhs.rankPercentOfPersonsWhoAre)
                .append(percentOfFemalesWhoAreOver, rhs.percentOfFemalesWhoAreOver)
                .append(rankPercentOfFemalesWhoAre, rhs.rankPercentOfFemalesWhoAre)
                .append(percentOfMalesWhoAreOverwe, rhs.percentOfMalesWhoAreOverwe)
                .append(rankPercentOfMalesWhoAreO, rhs.rankPercentOfMalesWhoAreO)
                .append(percentOfPersonsWhoAreObes, rhs.percentOfPersonsWhoAreObes)
                .append(rankPercentOfPersonsWhoA1, rhs.rankPercentOfPersonsWhoA1)
                .append(percentOfFemalesWhoAreObes, rhs.percentOfFemalesWhoAreObes)
                .append(rankPercentOfFemalesWhoA1, rhs.rankPercentOfFemalesWhoA1)
                .append(percentOfMalesWhoAreObese, rhs.percentOfMalesWhoAreObese)
                .append(rankPercentOfMalesWhoAre1, rhs.rankPercentOfMalesWhoAre1)
                .append(malignantCancersDiagnosedPer, rhs.malignantCancersDiagnosedPer)
                .append(rankMalignantCancersDiagnose, rhs.rankMalignantCancersDiagnose)
                .append(maleCancerIncidencePer1000, rhs.maleCancerIncidencePer1000)
                .append(rankMaleCancerIncidencePer, rhs.rankMaleCancerIncidencePer)
                .append(femaleCancerIncidencePer100, rhs.femaleCancerIncidencePer100)
                .append(rankFemaleCancerIncidencePe, rhs.rankFemaleCancerIncidencePe)
                .append(percentPoorDentalHealth, rhs.percentPoorDentalHealth)
                .append(rankPercentPoorDentalHealth, rhs.rankPercentPoorDentalHealth)
                .append(notificationsPer100000PopOf, rhs.notificationsPer100000PopOf)
                .append(rankNotificationsPer100000P, rhs.rankNotificationsPer100000P)
                .append(notificationsPer100000Pop1, rhs.notificationsPer100000Pop1)
                .append(rankNotificationsPer1000001, rhs.rankNotificationsPer1000001)
                .append(notificationsPer100000People, rhs.notificationsPer100000People)
                .append(rankNotificationsPer1000002, rhs.rankNotificationsPer1000002)
                .append(percentOfPersons18yrsPlusWh, rhs.percentOfPersons18yrsPlusWh)
                .append(rankPercentOfPersons18yrsPl, rhs.rankPercentOfPersons18yrsPl)
                .append(percentOfMales18yrsPlusWho, rhs.percentOfMales18yrsPlusWho)
                .append(rankPercentOfMales18yrsPlus, rhs.rankPercentOfMales18yrsPlus)
                .append(percentOfFemales18yrsPlusWh, rhs.percentOfFemales18yrsPlusWh)
                .append(rankPercentOfFemales18yrsPl, rhs.rankPercentOfFemales18yrsPl)
                .append(consumedAlcoholAtLeastWeekl, rhs.consumedAlcoholAtLeastWeekl)
                .append(rankConsumedAlcoholAtLeast, rhs.rankConsumedAlcoholAtLeast)
                .append(consumedAlcoholAtLeastWee1, rhs.consumedAlcoholAtLeastWee1)
                .append(rankConsumedAlcoholAtLeast1, rhs.rankConsumedAlcoholAtLeast1)
                .append(percentOfPersonsWhoDoNotM, rhs.percentOfPersonsWhoDoNotM)
                .append(rankPercentOfPersonsWhoDo, rhs.rankPercentOfPersonsWhoDo)
                .append(percentOfMalesWhoDoNotMee, rhs.percentOfMalesWhoDoNotMee)
                .append(rankPercentOfMalesWhoDoNo, rhs.rankPercentOfMalesWhoDoNo)
                .append(percentOfFemalesWhoDoNotM, rhs.percentOfFemalesWhoDoNotM)
                .append(rankPercentOfFemalesWhoDo, rhs.rankPercentOfFemalesWhoDo)
                .append(percentOfPersonsWhoDrinkSo, rhs.percentOfPersonsWhoDrinkSo)
                .append(rankPercentOfPersonsWhoDri, rhs.rankPercentOfPersonsWhoDri)
                .append(percentOfPersonsWhoShareA, rhs.percentOfPersonsWhoShareA)
                .append(rankPercentOfPersonsWhoSha, rhs.rankPercentOfPersonsWhoSha)
                .append(percentOfPersonsWhoDoNot1, rhs.percentOfPersonsWhoDoNot1)
                .append(rankPercentOfPersonsWhoDo1, rhs.rankPercentOfPersonsWhoDo1)
                .append(percentOfMalesWhoDoNotM1, rhs.percentOfMalesWhoDoNotM1)
                .append(rankPercentOfMalesWhoDo1, rhs.rankPercentOfMalesWhoDo1)
                .append(percentOfFemalesWhoDoNot1, rhs.percentOfFemalesWhoDoNot1)
                .append(rankPercentOfFemalesWhoDo1, rhs.rankPercentOfFemalesWhoDo1)
                .append(percentOfPersonsWhoSitFor, rhs.percentOfPersonsWhoSitFor)
                .append(rankPercentOfPersonsWhoSit, rhs.rankPercentOfPersonsWhoSit)
                .append(percentOfPersonsWhoVisitA, rhs.percentOfPersonsWhoVisitA)
                .append(rankPercentOfPersonsWhoVis, rhs.rankPercentOfPersonsWhoVis)
                .append(percentOfBreastScreeningPar, rhs.percentOfBreastScreeningPar)
                .append(rankPercentOfBreastScreenin, rhs.rankPercentOfBreastScreenin)
                .append(percentOfCervicalCancerScre, rhs.percentOfCervicalCancerScre)
                .append(rankPercentOfCervicalCancer, rhs.rankPercentOfCervicalCancer)
                .append(bowelCancerScreeningParticip, rhs.bowelCancerScreeningParticip)
                .append(rankBowelCancerScreeningPar, rhs.rankBowelCancerScreeningPar)
                .append(bowelCancerScreeningPartic1, rhs.bowelCancerScreeningPartic1)
                .append(rankBowelCancerScreeningP1, rhs.rankBowelCancerScreeningP1)
                .append(bowelCancerScreeningPartic2, rhs.bowelCancerScreeningPartic2)
                .append(rankBowelCancerScreeningP2, rhs.rankBowelCancerScreeningP2)
                .append(lowBirthweightBabies, rhs.lowBirthweightBabies)
                .append(rankLowBirthweightBabies, rhs.rankLowBirthweightBabies)
                .append(percentInfantsFullyBreastfed, rhs.percentInfantsFullyBreastfed)
                .append(rankPercentInfantsFullyBrea, rhs.rankPercentInfantsFullyBrea)
                .append(percentChildrenFullyImmunise, rhs.percentChildrenFullyImmunise)
                .append(rankPercentChildrenFullyImm, rhs.rankPercentChildrenFullyImm)
                .append(proportionOfInfantsEnrolled, rhs.proportionOfInfantsEnrolled)
                .append(rankProportionOfInfantsEnro, rhs.rankProportionOfInfantsEnro)
                .append(kindergartenParticipationRate, rhs.kindergartenParticipationRate)
                .append(percentOfChildrenWithKinder, rhs.percentOfChildrenWithKinder)
                .append(rankPercentOfChildrenWithK, rhs.rankPercentOfChildrenWithK)
                .append(percentOfChildrenWithEmotio, rhs.percentOfChildrenWithEmotio)
                .append(rankPercentOfChildrenWithE, rhs.rankPercentOfChildrenWithE)
                .append(percentOfChildrenWithSpeech, rhs.percentOfChildrenWithSpeech)
                .append(rankPercentOfChildrenWithS, rhs.rankPercentOfChildrenWithS)
                .append(percentOfAdolescentsWhoRepo, rhs.percentOfAdolescentsWhoRepo)
                .append(rankPercentOfAdolescentsWho, rhs.rankPercentOfAdolescentsWho)
                .append(percentOfChildrenWhoAreDev, rhs.percentOfChildrenWhoAreDev)
                .append(rankPercentOfChildrenWhoAr, rhs.rankPercentOfChildrenWhoAr)
                .append(percentOfChildrenWhoAreD1, rhs.percentOfChildrenWhoAreD1)
                .append(rankPercentOfChildrenWho1, rhs.rankPercentOfChildrenWho1)
                .append(coreActivityNeedForAssistan, rhs.coreActivityNeedForAssistan)
                .append(rankCoreActivityNeedForAss, rhs.rankCoreActivityNeedForAss)
                .append(peopleWithSevereAndProfound, rhs.peopleWithSevereAndProfound)
                .append(rankPeopleWithSevereAndPro, rhs.rankPeopleWithSevereAndPro)
                .append(peopleWithSevereAndProfou1, rhs.peopleWithSevereAndProfou1)
                .append(rankPeopleWithSevereAndP1, rhs.rankPeopleWithSevereAndP1)
                .append(percentPopAged75yrsPlusLivi, rhs.percentPopAged75yrsPlusLivi)
                .append(rankPercentPopAged75yrsPlus, rhs.rankPercentPopAged75yrsPlus)
                .append(popAged75yrsPlusLivingAlone, rhs.popAged75yrsPlusLivingAlone)
                .append(rankPopAged75yrsPlusLiving, rhs.rankPopAged75yrsPlusLiving)
                .append(popAged75yrsPlusLivingAlo1, rhs.popAged75yrsPlusLivingAlo1)
                .append(rankPopAged75yrsPlusLiving1, rhs.rankPopAged75yrsPlusLiving1)
                .append(personsReceivingDisabilitySe, rhs.personsReceivingDisabilitySe)
                .append(rankPersonsReceivingDisabili, rhs.rankPersonsReceivingDisabili)
                .append(disabilityPensionPer1000Eli, rhs.disabilityPensionPer1000Eli)
                .append(rankDisabilityPensionPer100, rhs.rankDisabilityPensionPer100)
                .append(agedCareHighCareBeds, rhs.agedCareHighCareBeds)
                .append(agedCareLowCareBeds, rhs.agedCareLowCareBeds)
                .append(agePensionPer1000EligibleP, rhs.agePensionPer1000EligibleP)
                .append(rankAgePensionPer1000Eligi, rhs.rankAgePensionPer1000Eligi)
                .append(maleLifeExpectancy, rhs.maleLifeExpectancy)
                .append(rankMaleLifeExpectancy, rhs.rankMaleLifeExpectancy)
                .append(femaleLifeExpectancy, rhs.femaleLifeExpectancy)
                .append(rankFemaleLifeExpectancy, rhs.rankFemaleLifeExpectancy)
                .append(personsReportingFairOrPoor, rhs.personsReportingFairOrPoor)
                .append(rankPersonsReportingFairOr, rhs.rankPersonsReportingFairOr)
                .append(femalesReportingFairOrPoor, rhs.femalesReportingFairOrPoor)
                .append(rankFemalesReportingFairOr, rhs.rankFemalesReportingFairOr)
                .append(malesReportingFairOrPoorHe, rhs.malesReportingFairOrPoorHe)
                .append(rankMalesReportingFairOrPo, rhs.rankMalesReportingFairOrPo)
                .append(percentWhoHaveAHighDegree, rhs.percentWhoHaveAHighDegree)
                .append(rankPercentWhoHaveAHighDe, rhs.rankPercentWhoHaveAHighDe)
                .append(percentOfPersonsSleepingLes, rhs.percentOfPersonsSleepingLes)
                .append(rankPercentOfPersonsSleepin, rhs.rankPercentOfPersonsSleepin)
                .append(percentPersonsWithAdequateW, rhs.percentPersonsWithAdequateW)
                .append(rankPercentPersonsWithAdequ, rhs.rankPercentPersonsWithAdequ)
                .append(unintentionalInjuriesTreated, rhs.unintentionalInjuriesTreated)
                .append(rankUnintentionalInjuriesTre, rhs.rankUnintentionalInjuriesTre)
                .append(intentionalInjuriesTreatedIn, rhs.intentionalInjuriesTreatedIn)
                .append(rankIntentionalInjuriesTreat, rhs.rankIntentionalInjuriesTreat)
                .append(percentOfUnintentionalHospit, rhs.percentOfUnintentionalHospit)
                .append(rankPercentOfUnintentionalH, rhs.rankPercentOfUnintentionalH)
                .append(indirectStandardisedDeathRat, rhs.indirectStandardisedDeathRat)
                .append(rankIndirectStandardisedDeat, rhs.rankIndirectStandardisedDeat)
                .append(avoidableDeaths0to74YrsFor, rhs.avoidableDeaths0to74YrsFor)
                .append(rankAvoidableDeaths0to74Yrs, rhs.rankAvoidableDeaths0to74Yrs)
                .append(avoidableDeaths0to74yrsrsFor, rhs.avoidableDeaths0to74yrsrsFor)
                .append(rankAvoidableDeaths0to74yrsr, rhs.rankAvoidableDeaths0to74yrsr)
                .append(avoidableDeaths0to74yrsrsF1, rhs.avoidableDeaths0to74yrsrsF1)
                .append(rankAvoidableDeaths0to74yr1, rhs.rankAvoidableDeaths0to74yr1)
                .append(avoidableDeaths0to74yrsForR, rhs.avoidableDeaths0to74yrsForR)
                .append(rankAvoidableDeaths0to74yrs, rhs.rankAvoidableDeaths0to74yrs)
                .append(primaryHealthNetwork, rhs.primaryHealthNetwork)
                .append(primaryCarePartnership, rhs.primaryCarePartnership)
                .append(numberOfHospitalsAndHealth, rhs.numberOfHospitalsAndHealth)
                .append(numberOfPublicHospitalsAnd, rhs.numberOfPublicHospitalsAnd)
                .append(numberOfPrivateHospitalsAnd, rhs.numberOfPrivateHospitalsAnd)
                .append(gPsPer1000Pop, rhs.gPsPer1000Pop)
                .append(rankGPsPer1000Pop, rhs.rankGPsPer1000Pop)
                .append(gPSitesPer1000Pop, rhs.gPSitesPer1000Pop)
                .append(rankGPSitesPer1000Pop, rhs.rankGPSitesPer1000Pop)
                .append(alliedHealthSitesPer1000Po, rhs.alliedHealthSitesPer1000Po)
                .append(rankAlliedHealthSitesPer10, rhs.rankAlliedHealthSitesPer10)
                .append(dentalServicesPer1000Pop, rhs.dentalServicesPer1000Pop)
                .append(rankDentalServicesPer1000P, rhs.rankDentalServicesPer1000P)
                .append(pharmaciesPer1000Pop, rhs.pharmaciesPer1000Pop)
                .append(rankPharmaciesPer1000Pop, rhs.rankPharmaciesPer1000Pop)
                .append(percentNearPublicTransport, rhs.percentNearPublicTransport)
                .append(rankPercentNearPublicTransp, rhs.rankPercentNearPublicTransp)
                .append(percentWithPrivateHealthIns, rhs.percentWithPrivateHealthIns)
                .append(rankPercentWithPrivateHealt, rhs.rankPercentWithPrivateHealt)
                .append(hospitalInpatientSeparations, rhs.hospitalInpatientSeparations)
                .append(rankHospitalInpatientSeparat, rhs.rankHospitalInpatientSeparat)
                .append(percentInpatientSeparationsF, rhs.percentInpatientSeparationsF)
                .append(rankPercentInpatientSeparati, rhs.rankPercentInpatientSeparati)
                .append(mainPublicHospitalAttendedF, rhs.mainPublicHospitalAttendedF)
                .append(mainPublicHospitalAttendedP, rhs.mainPublicHospitalAttendedP)
                .append(rankMainPublicHospitalAtten, rhs.rankMainPublicHospitalAtten)
                .append(averageLengthOfStayInDays, rhs.averageLengthOfStayInDays)
                .append(rankAverageLengthOfStayIn, rhs.rankAverageLengthOfStayIn)
                .append(averageLengthOfStayForAll, rhs.averageLengthOfStayForAll)
                .append(rankAverageLengthOfStayFor, rhs.rankAverageLengthOfStayFor)
                .append(perAnnumPercentChangeInSep, rhs.perAnnumPercentChangeInSep)
                .append(rankPerAnnumPercentChangeI, rhs.rankPerAnnumPercentChangeI)
                .append(perAnnumPercentProjectedCha, rhs.perAnnumPercentProjectedCha)
                .append(rankPerAnnumPercentProjecte, rhs.rankPerAnnumPercentProjecte)
                .append(aCSCsPer1000PopTotal, rhs.aCSCsPer1000PopTotal)
                .append(rankACSCsPer1000PopTotal, rhs.rankACSCsPer1000PopTotal)
                .append(aCSCsPer1000PopAcute, rhs.aCSCsPer1000PopAcute)
                .append(rankACSCsPer1000PopAcute, rhs.rankACSCsPer1000PopAcute)
                .append(aCSCsPer1000PopChronic, rhs.aCSCsPer1000PopChronic)
                .append(rankACSCsPer1000PopChronic, rhs.rankACSCsPer1000PopChronic)
                .append(aCSCsPer1000PopVaccinePrev, rhs.aCSCsPer1000PopVaccinePrev)
                .append(rankACSCsPer1000PopVaccine, rhs.rankACSCsPer1000PopVaccine)
                .append(emergencyDepartmentPresentati, rhs.emergencyDepartmentPresentati)
                .append(rankEmergencyDepartmentPrese, rhs.rankEmergencyDepartmentPrese)
                .append(primaryCareTypePresentations, rhs.primaryCareTypePresentations)
                .append(rankPrimaryCareTypePresenta, rhs.rankPrimaryCareTypePresenta)
                .append(childProtectionInvestigations, rhs.childProtectionInvestigations)
                .append(rankChildProtectionInvestiga, rhs.rankChildProtectionInvestiga)
                .append(childProtectionSubstantiation, rhs.childProtectionSubstantiation)
                .append(rankChildProtectionSubstanti, rhs.rankChildProtectionSubstanti)
                .append(numberOfChildFIRSTAssessmen, rhs.numberOfChildFIRSTAssessmen)
                .append(rankNumberOfChildFIRSTAsse, rhs.rankNumberOfChildFIRSTAsse)
                .append(gPAttendancesPer1000PopMal, rhs.gPAttendancesPer1000PopMal)
                .append(rankGPAttendancesPer1000Po, rhs.rankGPAttendancesPer1000Po)
                .append(gPAttendancesPer1000PopFem, rhs.gPAttendancesPer1000PopFem)
                .append(rankGPAttendancesPer10001, rhs.rankGPAttendancesPer10001)
                .append(gPAttendancesPer1000PopTot, rhs.gPAttendancesPer1000PopTot)
                .append(rankGPAttendancesPer10002, rhs.rankGPAttendancesPer10002)
                .append(hACCClientsAged0to64yrsPer, rhs.hACCClientsAged0to64yrsPer)
                .append(rankHACCClientsAged0to64yrs, rhs.rankHACCClientsAged0to64yrs)
                .append(hACCClientsAged65yrsPlusPer, rhs.hACCClientsAged65yrsPlusPer)
                .append(rankHACCClientsAged65yrsPlu, rhs.rankHACCClientsAged65yrsPlu)
                .append(noClientsWhoReceivedAlcohol, rhs.noClientsWhoReceivedAlcohol)
                .append(rankNoClientsWhoReceivedAl, rhs.rankNoClientsWhoReceivedAl)
                .append(registeredMentalMealthClient, rhs.registeredMentalMealthClient)
                .append(rankRegisteredMentalMealthC, rhs.rankRegisteredMentalMealthC)
                .isEquals();
    }
}
