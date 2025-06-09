/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */

package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    /** (Required) */
    @SerializedName("LGA")
    @Expose
    private Object lga;
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

    /** (Required) */
    public Object getLga() {
        return lga;
    }

    /** (Required) */
    public void setLga(Object lga) {
        this.lga = lga;
    }

    /** (Required) */
    public Object getMetropolitanRural() {
        return metropolitanRural;
    }

    /** (Required) */
    public void setMetropolitanRural(Object metropolitanRural) {
        this.metropolitanRural = metropolitanRural;
    }

    /** (Required) */
    public Object getDepartmentalRegion() {
        return departmentalRegion;
    }

    /** (Required) */
    public void setDepartmentalRegion(Object departmentalRegion) {
        this.departmentalRegion = departmentalRegion;
    }

    /** (Required) */
    public Object getDepartmentalArea() {
        return departmentalArea;
    }

    /** (Required) */
    public void setDepartmentalArea(Object departmentalArea) {
        this.departmentalArea = departmentalArea;
    }

    /** (Required) */
    public Object getAreaOfLGASqKm() {
        return areaOfLGASqKm;
    }

    /** (Required) */
    public void setAreaOfLGASqKm(Object areaOfLGASqKm) {
        this.areaOfLGASqKm = areaOfLGASqKm;
    }

    public Object getASGSLGACode() {
        return aSGSLGACode;
    }

    public void setASGSLGACode(Object aSGSLGACode) {
        this.aSGSLGACode = aSGSLGACode;
    }

    /** (Required) */
    public Object getMostPopulousTownOrSuburbIn() {
        return mostPopulousTownOrSuburbIn;
    }

    /** (Required) */
    public void setMostPopulousTownOrSuburbIn(Object mostPopulousTownOrSuburbIn) {
        this.mostPopulousTownOrSuburbIn = mostPopulousTownOrSuburbIn;
    }

    /** (Required) */
    public Object getDistanceToMelbourneInKm() {
        return distanceToMelbourneInKm;
    }

    /** (Required) */
    public void setDistanceToMelbourneInKm(Object distanceToMelbourneInKm) {
        this.distanceToMelbourneInKm = distanceToMelbourneInKm;
    }

    /** (Required) */
    public Object getTravelTimeFromMelbourneGPO() {
        return travelTimeFromMelbourneGPO;
    }

    /** (Required) */
    public void setTravelTimeFromMelbourneGPO(Object travelTimeFromMelbourneGPO) {
        this.travelTimeFromMelbourneGPO = travelTimeFromMelbourneGPO;
    }

    public Object getARIARemotenessCategory() {
        return aRIARemotenessCategory;
    }

    public void setARIARemotenessCategory(Object aRIARemotenessCategory) {
        this.aRIARemotenessCategory = aRIARemotenessCategory;
    }

    /** (Required) */
    public Object getPercentBusinessLandUse() {
        return percentBusinessLandUse;
    }

    /** (Required) */
    public void setPercentBusinessLandUse(Object percentBusinessLandUse) {
        this.percentBusinessLandUse = percentBusinessLandUse;
    }

    /** (Required) */
    public Object getPercentIndustrialLandUse() {
        return percentIndustrialLandUse;
    }

    /** (Required) */
    public void setPercentIndustrialLandUse(Object percentIndustrialLandUse) {
        this.percentIndustrialLandUse = percentIndustrialLandUse;
    }

    /** (Required) */
    public Object getPercentResidentialLandUse() {
        return percentResidentialLandUse;
    }

    /** (Required) */
    public void setPercentResidentialLandUse(Object percentResidentialLandUse) {
        this.percentResidentialLandUse = percentResidentialLandUse;
    }

    /** (Required) */
    public Object getPercentRuralLandUse() {
        return percentRuralLandUse;
    }

    /** (Required) */
    public void setPercentRuralLandUse(Object percentRuralLandUse) {
        this.percentRuralLandUse = percentRuralLandUse;
    }

    /** (Required) */
    public Object getPercentOtherLandUse() {
        return percentOtherLandUse;
    }

    /** (Required) */
    public void setPercentOtherLandUse(Object percentOtherLandUse) {
        this.percentOtherLandUse = percentOtherLandUse;
    }

    /** (Required) */
    public Object getPerAnnumPopChangeActualFor() {
        return perAnnumPopChangeActualFor;
    }

    /** (Required) */
    public void setPerAnnumPopChangeActualFor(Object perAnnumPopChangeActualFor) {
        this.perAnnumPopChangeActualFor = perAnnumPopChangeActualFor;
    }

    /** (Required) */
    public Object getPerAnnumPopChangeProjected() {
        return perAnnumPopChangeProjected;
    }

    /** (Required) */
    public void setPerAnnumPopChangeProjected(Object perAnnumPopChangeProjected) {
        this.perAnnumPopChangeProjected = perAnnumPopChangeProjected;
    }

    /** (Required) */
    public Object getFemales0to14yrs() {
        return females0to14yrs;
    }

    /** (Required) */
    public void setFemales0to14yrs(Object females0to14yrs) {
        this.females0to14yrs = females0to14yrs;
    }

    /** (Required) */
    public Object getFemales15to24yrs() {
        return females15to24yrs;
    }

    /** (Required) */
    public void setFemales15to24yrs(Object females15to24yrs) {
        this.females15to24yrs = females15to24yrs;
    }

    /** (Required) */
    public Object getFemales25to44yrs() {
        return females25to44yrs;
    }

    /** (Required) */
    public void setFemales25to44yrs(Object females25to44yrs) {
        this.females25to44yrs = females25to44yrs;
    }

    /** (Required) */
    public Object getFemales45to64yrs() {
        return females45to64yrs;
    }

    /** (Required) */
    public void setFemales45to64yrs(Object females45to64yrs) {
        this.females45to64yrs = females45to64yrs;
    }

    /** (Required) */
    public Object getFemales65to84yrs() {
        return females65to84yrs;
    }

    /** (Required) */
    public void setFemales65to84yrs(Object females65to84yrs) {
        this.females65to84yrs = females65to84yrs;
    }

    /** (Required) */
    public Object getFemale85yrsPlus() {
        return female85yrsPlus;
    }

    /** (Required) */
    public void setFemale85yrsPlus(Object female85yrsPlus) {
        this.female85yrsPlus = female85yrsPlus;
    }

    /** (Required) */
    public Object getTotalFemales() {
        return totalFemales;
    }

    /** (Required) */
    public void setTotalFemales(Object totalFemales) {
        this.totalFemales = totalFemales;
    }

    /** (Required) */
    public Object getMales0to14yrs() {
        return males0to14yrs;
    }

    /** (Required) */
    public void setMales0to14yrs(Object males0to14yrs) {
        this.males0to14yrs = males0to14yrs;
    }

    /** (Required) */
    public Object getMales15to24yrs() {
        return males15to24yrs;
    }

    /** (Required) */
    public void setMales15to24yrs(Object males15to24yrs) {
        this.males15to24yrs = males15to24yrs;
    }

    /** (Required) */
    public Object getMales25to44yrs() {
        return males25to44yrs;
    }

    /** (Required) */
    public void setMales25to44yrs(Object males25to44yrs) {
        this.males25to44yrs = males25to44yrs;
    }

    /** (Required) */
    public Object getMales45to64yrs() {
        return males45to64yrs;
    }

    /** (Required) */
    public void setMales45to64yrs(Object males45to64yrs) {
        this.males45to64yrs = males45to64yrs;
    }

    /** (Required) */
    public Object getMales65to84yrs() {
        return males65to84yrs;
    }

    /** (Required) */
    public void setMales65to84yrs(Object males65to84yrs) {
        this.males65to84yrs = males65to84yrs;
    }

    /** (Required) */
    public Object getMale85yrsPlus() {
        return male85yrsPlus;
    }

    /** (Required) */
    public void setMale85yrsPlus(Object male85yrsPlus) {
        this.male85yrsPlus = male85yrsPlus;
    }

    /** (Required) */
    public Object getTotalMales() {
        return totalMales;
    }

    /** (Required) */
    public void setTotalMales(Object totalMales) {
        this.totalMales = totalMales;
    }

    /** (Required) */
    public Object getTotal0to14yrs() {
        return total0to14yrs;
    }

    /** (Required) */
    public void setTotal0to14yrs(Object total0to14yrs) {
        this.total0to14yrs = total0to14yrs;
    }

    /** (Required) */
    public Object getTota15to24yrs() {
        return tota15to24yrs;
    }

    /** (Required) */
    public void setTota15to24yrs(Object tota15to24yrs) {
        this.tota15to24yrs = tota15to24yrs;
    }

    /** (Required) */
    public Object getTota25to44yrs() {
        return tota25to44yrs;
    }

    /** (Required) */
    public void setTota25to44yrs(Object tota25to44yrs) {
        this.tota25to44yrs = tota25to44yrs;
    }

    /** (Required) */
    public Object getTota45to64yrs() {
        return tota45to64yrs;
    }

    /** (Required) */
    public void setTota45to64yrs(Object tota45to64yrs) {
        this.tota45to64yrs = tota45to64yrs;
    }

    /** (Required) */
    public Object getTota65to84yrs() {
        return tota65to84yrs;
    }

    /** (Required) */
    public void setTota65to84yrs(Object tota65to84yrs) {
        this.tota65to84yrs = tota65to84yrs;
    }

    /** (Required) */
    public Object getTota85yrsPlus() {
        return tota85yrsPlus;
    }

    /** (Required) */
    public void setTota85yrsPlus(Object tota85yrsPlus) {
        this.tota85yrsPlus = tota85yrsPlus;
    }

    /** (Required) */
    public Object getTotal2013ERP() {
        return total2013ERP;
    }

    /** (Required) */
    public void setTotal2013ERP(Object total2013ERP) {
        this.total2013ERP = total2013ERP;
    }

    /** (Required) */
    public Object getPercentTotal0to14yrs() {
        return percentTotal0to14yrs;
    }

    /** (Required) */
    public void setPercentTotal0to14yrs(Object percentTotal0to14yrs) {
        this.percentTotal0to14yrs = percentTotal0to14yrs;
    }

    /** (Required) */
    public Object getPercentTotal15to24yrs() {
        return percentTotal15to24yrs;
    }

    /** (Required) */
    public void setPercentTotal15to24yrs(Object percentTotal15to24yrs) {
        this.percentTotal15to24yrs = percentTotal15to24yrs;
    }

    /** (Required) */
    public Object getPercentTotal25to44yrs() {
        return percentTotal25to44yrs;
    }

    /** (Required) */
    public void setPercentTotal25to44yrs(Object percentTotal25to44yrs) {
        this.percentTotal25to44yrs = percentTotal25to44yrs;
    }

    /** (Required) */
    public Object getPercentTotal45to64yrs() {
        return percentTotal45to64yrs;
    }

    /** (Required) */
    public void setPercentTotal45to64yrs(Object percentTotal45to64yrs) {
        this.percentTotal45to64yrs = percentTotal45to64yrs;
    }

    /** (Required) */
    public Object getPercentTotal65to84yrs() {
        return percentTotal65to84yrs;
    }

    /** (Required) */
    public void setPercentTotal65to84yrs(Object percentTotal65to84yrs) {
        this.percentTotal65to84yrs = percentTotal65to84yrs;
    }

    /** (Required) */
    public Object getPercentTotal84yrsPlus() {
        return percentTotal84yrsPlus;
    }

    /** (Required) */
    public void setPercentTotal84yrsPlus(Object percentTotal84yrsPlus) {
        this.percentTotal84yrsPlus = percentTotal84yrsPlus;
    }

    /** (Required) */
    public Object getTotalFertilityRate2012() {
        return totalFertilityRate2012;
    }

    /** (Required) */
    public void setTotalFertilityRate2012(Object totalFertilityRate2012) {
        this.totalFertilityRate2012 = totalFertilityRate2012;
    }

    /** (Required) */
    public Object getRankTotalFertilityRate2012() {
        return rankTotalFertilityRate2012;
    }

    /** (Required) */
    public void setRankTotalFertilityRate2012(Object rankTotalFertilityRate2012) {
        this.rankTotalFertilityRate2012 = rankTotalFertilityRate2012;
    }

    /** (Required) */
    public Object getTeenageFertilityRate2012() {
        return teenageFertilityRate2012;
    }

    /** (Required) */
    public void setTeenageFertilityRate2012(Object teenageFertilityRate2012) {
        this.teenageFertilityRate2012 = teenageFertilityRate2012;
    }

    /** (Required) */
    public Object getRankTeenageFertilityRate201() {
        return rankTeenageFertilityRate201;
    }

    /** (Required) */
    public void setRankTeenageFertilityRate201(Object rankTeenageFertilityRate201) {
        this.rankTeenageFertilityRate201 = rankTeenageFertilityRate201;
    }

    /** (Required) */
    public Object getPercentAboriginalOrTorresSt() {
        return percentAboriginalOrTorresSt;
    }

    /** (Required) */
    public void setPercentAboriginalOrTorresSt(Object percentAboriginalOrTorresSt) {
        this.percentAboriginalOrTorresSt = percentAboriginalOrTorresSt;
    }

    /** (Required) */
    public Object getRankAboriginalOrTorresStrai() {
        return rankAboriginalOrTorresStrai;
    }

    /** (Required) */
    public void setRankAboriginalOrTorresStrai(Object rankAboriginalOrTorresStrai) {
        this.rankAboriginalOrTorresStrai = rankAboriginalOrTorresStrai;
    }

    /** (Required) */
    public Object getPercentBornOverseas2011() {
        return percentBornOverseas2011;
    }

    /** (Required) */
    public void setPercentBornOverseas2011(Object percentBornOverseas2011) {
        this.percentBornOverseas2011 = percentBornOverseas2011;
    }

    /** (Required) */
    public Object getRankPercentBornOverseas2011() {
        return rankPercentBornOverseas2011;
    }

    /** (Required) */
    public void setRankPercentBornOverseas2011(Object rankPercentBornOverseas2011) {
        this.rankPercentBornOverseas2011 = rankPercentBornOverseas2011;
    }

    /** (Required) */
    public Object getBornInANonEnglishSpeaking() {
        return bornInANonEnglishSpeaking;
    }

    /** (Required) */
    public void setBornInANonEnglishSpeaking(Object bornInANonEnglishSpeaking) {
        this.bornInANonEnglishSpeaking = bornInANonEnglishSpeaking;
    }

    /** (Required) */
    public Object getRankBornInANonEnglishSpea() {
        return rankBornInANonEnglishSpea;
    }

    /** (Required) */
    public void setRankBornInANonEnglishSpea(Object rankBornInANonEnglishSpea) {
        this.rankBornInANonEnglishSpea = rankBornInANonEnglishSpea;
    }

    /** (Required) */
    public Object getCountry1PercentForTop5Ove() {
        return country1PercentForTop5Ove;
    }

    /** (Required) */
    public void setCountry1PercentForTop5Ove(Object country1PercentForTop5Ove) {
        this.country1PercentForTop5Ove = country1PercentForTop5Ove;
    }

    /** (Required) */
    public Object getCountry1ForTop5OverseasCo() {
        return country1ForTop5OverseasCo;
    }

    /** (Required) */
    public void setCountry1ForTop5OverseasCo(Object country1ForTop5OverseasCo) {
        this.country1ForTop5OverseasCo = country1ForTop5OverseasCo;
    }

    /** (Required) */
    public Object getCountry2PercentForTop5Ove() {
        return country2PercentForTop5Ove;
    }

    /** (Required) */
    public void setCountry2PercentForTop5Ove(Object country2PercentForTop5Ove) {
        this.country2PercentForTop5Ove = country2PercentForTop5Ove;
    }

    /** (Required) */
    public Object getCountry2ForTop5OverseasCo() {
        return country2ForTop5OverseasCo;
    }

    /** (Required) */
    public void setCountry2ForTop5OverseasCo(Object country2ForTop5OverseasCo) {
        this.country2ForTop5OverseasCo = country2ForTop5OverseasCo;
    }

    /** (Required) */
    public Object getCountry3PercentForTop5Ove() {
        return country3PercentForTop5Ove;
    }

    /** (Required) */
    public void setCountry3PercentForTop5Ove(Object country3PercentForTop5Ove) {
        this.country3PercentForTop5Ove = country3PercentForTop5Ove;
    }

    /** (Required) */
    public Object getCountry3ForTop5OverseasCo() {
        return country3ForTop5OverseasCo;
    }

    /** (Required) */
    public void setCountry3ForTop5OverseasCo(Object country3ForTop5OverseasCo) {
        this.country3ForTop5OverseasCo = country3ForTop5OverseasCo;
    }

    /** (Required) */
    public Object getCountry4PercentForTop5Ove() {
        return country4PercentForTop5Ove;
    }

    /** (Required) */
    public void setCountry4PercentForTop5Ove(Object country4PercentForTop5Ove) {
        this.country4PercentForTop5Ove = country4PercentForTop5Ove;
    }

    /** (Required) */
    public Object getCountry4ForTop5OverseasCo() {
        return country4ForTop5OverseasCo;
    }

    /** (Required) */
    public void setCountry4ForTop5OverseasCo(Object country4ForTop5OverseasCo) {
        this.country4ForTop5OverseasCo = country4ForTop5OverseasCo;
    }

    /** (Required) */
    public Object getCountry5PercentForTop5Ove() {
        return country5PercentForTop5Ove;
    }

    /** (Required) */
    public void setCountry5PercentForTop5Ove(Object country5PercentForTop5Ove) {
        this.country5PercentForTop5Ove = country5PercentForTop5Ove;
    }

    /** (Required) */
    public Object getCountry5ForTop5OverseasCo() {
        return country5ForTop5OverseasCo;
    }

    /** (Required) */
    public void setCountry5ForTop5OverseasCo(Object country5ForTop5OverseasCo) {
        this.country5ForTop5OverseasCo = country5ForTop5OverseasCo;
    }

    /** (Required) */
    public Object getPercentSpeaksLOTEAtHome() {
        return percentSpeaksLOTEAtHome;
    }

    /** (Required) */
    public void setPercentSpeaksLOTEAtHome(Object percentSpeaksLOTEAtHome) {
        this.percentSpeaksLOTEAtHome = percentSpeaksLOTEAtHome;
    }

    /** (Required) */
    public Object getRankPercentSpeaksLOTEAtHom() {
        return rankPercentSpeaksLOTEAtHom;
    }

    /** (Required) */
    public void setRankPercentSpeaksLOTEAtHom(Object rankPercentSpeaksLOTEAtHom) {
        this.rankPercentSpeaksLOTEAtHom = rankPercentSpeaksLOTEAtHom;
    }

    /** (Required) */
    public Object getCountry1PercentTop5Languag() {
        return country1PercentTop5Languag;
    }

    /** (Required) */
    public void setCountry1PercentTop5Languag(Object country1PercentTop5Languag) {
        this.country1PercentTop5Languag = country1PercentTop5Languag;
    }

    /** (Required) */
    public Object getCountry1Top5LanguagesSpoke() {
        return country1Top5LanguagesSpoke;
    }

    /** (Required) */
    public void setCountry1Top5LanguagesSpoke(Object country1Top5LanguagesSpoke) {
        this.country1Top5LanguagesSpoke = country1Top5LanguagesSpoke;
    }

    /** (Required) */
    public Object getCountry2PercentTop5Languag() {
        return country2PercentTop5Languag;
    }

    /** (Required) */
    public void setCountry2PercentTop5Languag(Object country2PercentTop5Languag) {
        this.country2PercentTop5Languag = country2PercentTop5Languag;
    }

    /** (Required) */
    public Object getCountry2Top5LanguagesSpoke() {
        return country2Top5LanguagesSpoke;
    }

    /** (Required) */
    public void setCountry2Top5LanguagesSpoke(Object country2Top5LanguagesSpoke) {
        this.country2Top5LanguagesSpoke = country2Top5LanguagesSpoke;
    }

    /** (Required) */
    public Object getCountry3PercentTop5Languag() {
        return country3PercentTop5Languag;
    }

    /** (Required) */
    public void setCountry3PercentTop5Languag(Object country3PercentTop5Languag) {
        this.country3PercentTop5Languag = country3PercentTop5Languag;
    }

    /** (Required) */
    public Object getCountry3Top5LanguagesSpoke() {
        return country3Top5LanguagesSpoke;
    }

    /** (Required) */
    public void setCountry3Top5LanguagesSpoke(Object country3Top5LanguagesSpoke) {
        this.country3Top5LanguagesSpoke = country3Top5LanguagesSpoke;
    }

    /** (Required) */
    public Object getCountry4PercentTop5Languag() {
        return country4PercentTop5Languag;
    }

    /** (Required) */
    public void setCountry4PercentTop5Languag(Object country4PercentTop5Languag) {
        this.country4PercentTop5Languag = country4PercentTop5Languag;
    }

    /** (Required) */
    public Object getCountry4Top5LanguagesSpoke() {
        return country4Top5LanguagesSpoke;
    }

    /** (Required) */
    public void setCountry4Top5LanguagesSpoke(Object country4Top5LanguagesSpoke) {
        this.country4Top5LanguagesSpoke = country4Top5LanguagesSpoke;
    }

    /** (Required) */
    public Object getCountry5PercentTop5Languag() {
        return country5PercentTop5Languag;
    }

    /** (Required) */
    public void setCountry5PercentTop5Languag(Object country5PercentTop5Languag) {
        this.country5PercentTop5Languag = country5PercentTop5Languag;
    }

    /** (Required) */
    public Object getCountry5Top5LanguagesSpoke() {
        return country5Top5LanguagesSpoke;
    }

    /** (Required) */
    public void setCountry5Top5LanguagesSpoke(Object country5Top5LanguagesSpoke) {
        this.country5Top5LanguagesSpoke = country5Top5LanguagesSpoke;
    }

    /** (Required) */
    public Object getPercentLowEnglishProficiency() {
        return percentLowEnglishProficiency;
    }

    /** (Required) */
    public void setPercentLowEnglishProficiency(Object percentLowEnglishProficiency) {
        this.percentLowEnglishProficiency = percentLowEnglishProficiency;
    }

    /** (Required) */
    public Object getRankPercentLowEnglishProfic() {
        return rankPercentLowEnglishProfic;
    }

    /** (Required) */
    public void setRankPercentLowEnglishProfic(Object rankPercentLowEnglishProfic) {
        this.rankPercentLowEnglishProfic = rankPercentLowEnglishProfic;
    }

    /** (Required) */
    public Object getAncestry1PercentTop5Ancest() {
        return ancestry1PercentTop5Ancest;
    }

    /** (Required) */
    public void setAncestry1PercentTop5Ancest(Object ancestry1PercentTop5Ancest) {
        this.ancestry1PercentTop5Ancest = ancestry1PercentTop5Ancest;
    }

    /** (Required) */
    public Object getAncestry1Top5Ancestries() {
        return ancestry1Top5Ancestries;
    }

    /** (Required) */
    public void setAncestry1Top5Ancestries(Object ancestry1Top5Ancestries) {
        this.ancestry1Top5Ancestries = ancestry1Top5Ancestries;
    }

    /** (Required) */
    public Object getAncestry2PercentTop5Ancest() {
        return ancestry2PercentTop5Ancest;
    }

    /** (Required) */
    public void setAncestry2PercentTop5Ancest(Object ancestry2PercentTop5Ancest) {
        this.ancestry2PercentTop5Ancest = ancestry2PercentTop5Ancest;
    }

    /** (Required) */
    public Object getAncestry2Top5Ancestries() {
        return ancestry2Top5Ancestries;
    }

    /** (Required) */
    public void setAncestry2Top5Ancestries(Object ancestry2Top5Ancestries) {
        this.ancestry2Top5Ancestries = ancestry2Top5Ancestries;
    }

    /** (Required) */
    public Object getAncestry3PercentTop5Ancest() {
        return ancestry3PercentTop5Ancest;
    }

    /** (Required) */
    public void setAncestry3PercentTop5Ancest(Object ancestry3PercentTop5Ancest) {
        this.ancestry3PercentTop5Ancest = ancestry3PercentTop5Ancest;
    }

    /** (Required) */
    public Object getAncestry3Top5Ancestries() {
        return ancestry3Top5Ancestries;
    }

    /** (Required) */
    public void setAncestry3Top5Ancestries(Object ancestry3Top5Ancestries) {
        this.ancestry3Top5Ancestries = ancestry3Top5Ancestries;
    }

    /** (Required) */
    public Object getAncestry4PercentTop5Ancest() {
        return ancestry4PercentTop5Ancest;
    }

    /** (Required) */
    public void setAncestry4PercentTop5Ancest(Object ancestry4PercentTop5Ancest) {
        this.ancestry4PercentTop5Ancest = ancestry4PercentTop5Ancest;
    }

    /** (Required) */
    public Object getAncestry4Top5Ancestries() {
        return ancestry4Top5Ancestries;
    }

    /** (Required) */
    public void setAncestry4Top5Ancestries(Object ancestry4Top5Ancestries) {
        this.ancestry4Top5Ancestries = ancestry4Top5Ancestries;
    }

    /** (Required) */
    public Object getAncestry5PercentTop5Ancest() {
        return ancestry5PercentTop5Ancest;
    }

    /** (Required) */
    public void setAncestry5PercentTop5Ancest(Object ancestry5PercentTop5Ancest) {
        this.ancestry5PercentTop5Ancest = ancestry5PercentTop5Ancest;
    }

    /** (Required) */
    public Object getAncestry5Top5Ancestries() {
        return ancestry5Top5Ancestries;
    }

    /** (Required) */
    public void setAncestry5Top5Ancestries(Object ancestry5Top5Ancestries) {
        this.ancestry5Top5Ancestries = ancestry5Top5Ancestries;
    }

    /** (Required) */
    public Object getNewSettlerArrivalsPer100000() {
        return newSettlerArrivalsPer100000;
    }

    /** (Required) */
    public void setNewSettlerArrivalsPer100000(Object newSettlerArrivalsPer100000) {
        this.newSettlerArrivalsPer100000 = newSettlerArrivalsPer100000;
    }

    /** (Required) */
    public Object getRankNewSettlerArrivalsPer1() {
        return rankNewSettlerArrivalsPer1;
    }

    /** (Required) */
    public void setRankNewSettlerArrivalsPer1(Object rankNewSettlerArrivalsPer1) {
        this.rankNewSettlerArrivalsPer1 = rankNewSettlerArrivalsPer1;
    }

    /** (Required) */
    public Object getHumanitarianArrivalsAsAPerc() {
        return humanitarianArrivalsAsAPerc;
    }

    /** (Required) */
    public void setHumanitarianArrivalsAsAPerc(Object humanitarianArrivalsAsAPerc) {
        this.humanitarianArrivalsAsAPerc = humanitarianArrivalsAsAPerc;
    }

    /** (Required) */
    public Object getRankHumanitarianArrivalsAsA() {
        return rankHumanitarianArrivalsAsA;
    }

    /** (Required) */
    public void setRankHumanitarianArrivalsAsA(Object rankHumanitarianArrivalsAsA) {
        this.rankHumanitarianArrivalsAsA = rankHumanitarianArrivalsAsA;
    }

    /** (Required) */
    public Object getCommunityAcceptanceOfDiverse() {
        return communityAcceptanceOfDiverse;
    }

    /** (Required) */
    public void setCommunityAcceptanceOfDiverse(Object communityAcceptanceOfDiverse) {
        this.communityAcceptanceOfDiverse = communityAcceptanceOfDiverse;
    }

    /** (Required) */
    public Object getRankCommunityAcceptanceOfDi() {
        return rankCommunityAcceptanceOfDi;
    }

    /** (Required) */
    public void setRankCommunityAcceptanceOfDi(Object rankCommunityAcceptanceOfDi) {
        this.rankCommunityAcceptanceOfDi = rankCommunityAcceptanceOfDi;
    }

    /** (Required) */
    public Object getProportionOfHouseholdsWithB() {
        return proportionOfHouseholdsWithB;
    }

    /** (Required) */
    public void setProportionOfHouseholdsWithB(Object proportionOfHouseholdsWithB) {
        this.proportionOfHouseholdsWithB = proportionOfHouseholdsWithB;
    }

    /** (Required) */
    public Object getHouseholdsWithBroadbandInter() {
        return householdsWithBroadbandInter;
    }

    /** (Required) */
    public void setHouseholdsWithBroadbandInter(Object householdsWithBroadbandInter) {
        this.householdsWithBroadbandInter = householdsWithBroadbandInter;
    }

    /** (Required) */
    public Object getGamingMachineLossesPerHead() {
        return gamingMachineLossesPerHead;
    }

    /** (Required) */
    public void setGamingMachineLossesPerHead(Object gamingMachineLossesPerHead) {
        this.gamingMachineLossesPerHead = gamingMachineLossesPerHead;
    }

    /** (Required) */
    public Object getRankGamingMachineLossesPer() {
        return rankGamingMachineLossesPer;
    }

    /** (Required) */
    public void setRankGamingMachineLossesPer(Object rankGamingMachineLossesPer) {
        this.rankGamingMachineLossesPer = rankGamingMachineLossesPer;
    }

    /** (Required) */
    public Object getFamilyIncidentsPer1000Pop() {
        return familyIncidentsPer1000Pop;
    }

    /** (Required) */
    public void setFamilyIncidentsPer1000Pop(Object familyIncidentsPer1000Pop) {
        this.familyIncidentsPer1000Pop = familyIncidentsPer1000Pop;
    }

    /** (Required) */
    public Object getRankFamilyIncidentsPer1000() {
        return rankFamilyIncidentsPer1000;
    }

    /** (Required) */
    public void setRankFamilyIncidentsPer1000(Object rankFamilyIncidentsPer1000) {
        this.rankFamilyIncidentsPer1000 = rankFamilyIncidentsPer1000;
    }

    /** (Required) */
    public Object getDrugUsageAndPossessionOffen() {
        return drugUsageAndPossessionOffen;
    }

    /** (Required) */
    public void setDrugUsageAndPossessionOffen(Object drugUsageAndPossessionOffen) {
        this.drugUsageAndPossessionOffen = drugUsageAndPossessionOffen;
    }

    /** (Required) */
    public Object getRankDrugUsageAndPossession() {
        return rankDrugUsageAndPossession;
    }

    /** (Required) */
    public void setRankDrugUsageAndPossession(Object rankDrugUsageAndPossession) {
        this.rankDrugUsageAndPossession = rankDrugUsageAndPossession;
    }

    /** (Required) */
    public Object getTotalCrimePer1000Pop() {
        return totalCrimePer1000Pop;
    }

    /** (Required) */
    public void setTotalCrimePer1000Pop(Object totalCrimePer1000Pop) {
        this.totalCrimePer1000Pop = totalCrimePer1000Pop;
    }

    /** (Required) */
    public Object getRankTotalCrimePer1000Pop() {
        return rankTotalCrimePer1000Pop;
    }

    /** (Required) */
    public void setRankTotalCrimePer1000Pop(Object rankTotalCrimePer1000Pop) {
        this.rankTotalCrimePer1000Pop = rankTotalCrimePer1000Pop;
    }

    /** (Required) */
    public Object getFeelsSafeWalkingAloneDuring() {
        return feelsSafeWalkingAloneDuring;
    }

    /** (Required) */
    public void setFeelsSafeWalkingAloneDuring(Object feelsSafeWalkingAloneDuring) {
        this.feelsSafeWalkingAloneDuring = feelsSafeWalkingAloneDuring;
    }

    /** (Required) */
    public Object getRankFeelsSafeWalkingAloneD() {
        return rankFeelsSafeWalkingAloneD;
    }

    /** (Required) */
    public void setRankFeelsSafeWalkingAloneD(Object rankFeelsSafeWalkingAloneD) {
        this.rankFeelsSafeWalkingAloneD = rankFeelsSafeWalkingAloneD;
    }

    /** (Required) */
    public Object getBelieveOtherPeopleCanBeTru() {
        return believeOtherPeopleCanBeTru;
    }

    /** (Required) */
    public void setBelieveOtherPeopleCanBeTru(Object believeOtherPeopleCanBeTru) {
        this.believeOtherPeopleCanBeTru = believeOtherPeopleCanBeTru;
    }

    /** (Required) */
    public Object getRankBelieveOtherPeopleCanB() {
        return rankBelieveOtherPeopleCanB;
    }

    /** (Required) */
    public void setRankBelieveOtherPeopleCanB(Object rankBelieveOtherPeopleCanB) {
        this.rankBelieveOtherPeopleCanB = rankBelieveOtherPeopleCanB;
    }

    /** (Required) */
    public Object getSpokeWithMoreThan5PeopleT() {
        return spokeWithMoreThan5PeopleT;
    }

    /** (Required) */
    public void setSpokeWithMoreThan5PeopleT(Object spokeWithMoreThan5PeopleT) {
        this.spokeWithMoreThan5PeopleT = spokeWithMoreThan5PeopleT;
    }

    /** (Required) */
    public Object getRankSpokeWithMoreThan5Peo() {
        return rankSpokeWithMoreThan5Peo;
    }

    /** (Required) */
    public void setRankSpokeWithMoreThan5Peo(Object rankSpokeWithMoreThan5Peo) {
        this.rankSpokeWithMoreThan5Peo = rankSpokeWithMoreThan5Peo;
    }

    /** (Required) */
    public Object getAbleToDefinitelyGetHelpFro() {
        return ableToDefinitelyGetHelpFro;
    }

    /** (Required) */
    public void setAbleToDefinitelyGetHelpFro(Object ableToDefinitelyGetHelpFro) {
        this.ableToDefinitelyGetHelpFro = ableToDefinitelyGetHelpFro;
    }

    /** (Required) */
    public Object getRankAbleToDefinitelyGetHel() {
        return rankAbleToDefinitelyGetHel;
    }

    /** (Required) */
    public void setRankAbleToDefinitelyGetHel(Object rankAbleToDefinitelyGetHel) {
        this.rankAbleToDefinitelyGetHel = rankAbleToDefinitelyGetHel;
    }

    /** (Required) */
    public Object getVolunteers() {
        return volunteers;
    }

    /** (Required) */
    public void setVolunteers(Object volunteers) {
        this.volunteers = volunteers;
    }

    /** (Required) */
    public Object getRankVolunteers() {
        return rankVolunteers;
    }

    /** (Required) */
    public void setRankVolunteers(Object rankVolunteers) {
        this.rankVolunteers = rankVolunteers;
    }

    /** (Required) */
    public Object getFeelValuedBySociety() {
        return feelValuedBySociety;
    }

    /** (Required) */
    public void setFeelValuedBySociety(Object feelValuedBySociety) {
        this.feelValuedBySociety = feelValuedBySociety;
    }

    /** (Required) */
    public Object getRankFeelValuedBySociety() {
        return rankFeelValuedBySociety;
    }

    /** (Required) */
    public void setRankFeelValuedBySociety(Object rankFeelValuedBySociety) {
        this.rankFeelValuedBySociety = rankFeelValuedBySociety;
    }

    /** (Required) */
    public Object getAttendedALocalCommunityEven() {
        return attendedALocalCommunityEven;
    }

    /** (Required) */
    public void setAttendedALocalCommunityEven(Object attendedALocalCommunityEven) {
        this.attendedALocalCommunityEven = attendedALocalCommunityEven;
    }

    /** (Required) */
    public Object getRankAttendedALocalCommunity() {
        return rankAttendedALocalCommunity;
    }

    /** (Required) */
    public void setRankAttendedALocalCommunity(Object rankAttendedALocalCommunity) {
        this.rankAttendedALocalCommunity = rankAttendedALocalCommunity;
    }

    /** (Required) */
    public Object getTakeActionOnBehalfOfTheLo() {
        return takeActionOnBehalfOfTheLo;
    }

    /** (Required) */
    public void setTakeActionOnBehalfOfTheLo(Object takeActionOnBehalfOfTheLo) {
        this.takeActionOnBehalfOfTheLo = takeActionOnBehalfOfTheLo;
    }

    /** (Required) */
    public Object getRankTakeActionOnBehalfOfT() {
        return rankTakeActionOnBehalfOfT;
    }

    /** (Required) */
    public void setRankTakeActionOnBehalfOfT(Object rankTakeActionOnBehalfOfT) {
        this.rankTakeActionOnBehalfOfT = rankTakeActionOnBehalfOfT;
    }

    /** (Required) */
    public Object getMembersOfASportsGroup() {
        return membersOfASportsGroup;
    }

    /** (Required) */
    public void setMembersOfASportsGroup(Object membersOfASportsGroup) {
        this.membersOfASportsGroup = membersOfASportsGroup;
    }

    /** (Required) */
    public Object getRankMembersOfASportsGroup() {
        return rankMembersOfASportsGroup;
    }

    /** (Required) */
    public void setRankMembersOfASportsGroup(Object rankMembersOfASportsGroup) {
        this.rankMembersOfASportsGroup = rankMembersOfASportsGroup;
    }

    /** (Required) */
    public Object getMembersOfAReligiousGroup() {
        return membersOfAReligiousGroup;
    }

    /** (Required) */
    public void setMembersOfAReligiousGroup(Object membersOfAReligiousGroup) {
        this.membersOfAReligiousGroup = membersOfAReligiousGroup;
    }

    /** (Required) */
    public Object getRankMembersOfAReligiousGro() {
        return rankMembersOfAReligiousGro;
    }

    /** (Required) */
    public void setRankMembersOfAReligiousGro(Object rankMembersOfAReligiousGro) {
        this.rankMembersOfAReligiousGro = rankMembersOfAReligiousGro;
    }

    /** (Required) */
    public Object getRatedTheirCommunityAsAnAct() {
        return ratedTheirCommunityAsAnAct;
    }

    /** (Required) */
    public void setRatedTheirCommunityAsAnAct(Object ratedTheirCommunityAsAnAct) {
        this.ratedTheirCommunityAsAnAct = ratedTheirCommunityAsAnAct;
    }

    /** (Required) */
    public Object getRankRatedTheirCommunityAsA() {
        return rankRatedTheirCommunityAsA;
    }

    /** (Required) */
    public void setRankRatedTheirCommunityAsA(Object rankRatedTheirCommunityAsA) {
        this.rankRatedTheirCommunityAsA = rankRatedTheirCommunityAsA;
    }

    /** (Required) */
    public Object getRatedTheirCommunityAsAPlea() {
        return ratedTheirCommunityAsAPlea;
    }

    /** (Required) */
    public void setRatedTheirCommunityAsAPlea(Object ratedTheirCommunityAsAPlea) {
        this.ratedTheirCommunityAsAPlea = ratedTheirCommunityAsAPlea;
    }

    /** (Required) */
    public Object getRankAtedTheirCommunityAsA() {
        return rankAtedTheirCommunityAsA;
    }

    /** (Required) */
    public void setRankAtedTheirCommunityAsA(Object rankAtedTheirCommunityAsA) {
        this.rankAtedTheirCommunityAsA = rankAtedTheirCommunityAsA;
    }

    /** (Required) */
    public Object getRatedTheirCommunityAsGoodO() {
        return ratedTheirCommunityAsGoodO;
    }

    /** (Required) */
    public void setRatedTheirCommunityAsGoodO(Object ratedTheirCommunityAsGoodO) {
        this.ratedTheirCommunityAsGoodO = ratedTheirCommunityAsGoodO;
    }

    /** (Required) */
    public Object getRankRatedTheirCommunityAsG() {
        return rankRatedTheirCommunityAsG;
    }

    /** (Required) */
    public void setRankRatedTheirCommunityAsG(Object rankRatedTheirCommunityAsG) {
        this.rankRatedTheirCommunityAsG = rankRatedTheirCommunityAsG;
    }

    /** (Required) */
    public Object getIndexOfRelativeSociaEconomi() {
        return indexOfRelativeSociaEconomi;
    }

    /** (Required) */
    public void setIndexOfRelativeSociaEconomi(Object indexOfRelativeSociaEconomi) {
        this.indexOfRelativeSociaEconomi = indexOfRelativeSociaEconomi;
    }

    /** (Required) */
    public Object getRankIndexOfRelativeSociaEc() {
        return rankIndexOfRelativeSociaEc;
    }

    /** (Required) */
    public void setRankIndexOfRelativeSociaEc(Object rankIndexOfRelativeSociaEc) {
        this.rankIndexOfRelativeSociaEc = rankIndexOfRelativeSociaEc;
    }

    /** (Required) */
    public Object getUnemploymentRate() {
        return unemploymentRate;
    }

    /** (Required) */
    public void setUnemploymentRate(Object unemploymentRate) {
        this.unemploymentRate = unemploymentRate;
    }

    /** (Required) */
    public Object getRankUnemploymentRate() {
        return rankUnemploymentRate;
    }

    /** (Required) */
    public void setRankUnemploymentRate(Object rankUnemploymentRate) {
        this.rankUnemploymentRate = rankUnemploymentRate;
    }

    /** (Required) */
    public Object getPercentIndividualIncomeLess() {
        return percentIndividualIncomeLess;
    }

    /** (Required) */
    public void setPercentIndividualIncomeLess(Object percentIndividualIncomeLess) {
        this.percentIndividualIncomeLess = percentIndividualIncomeLess;
    }

    /** (Required) */
    public Object getRankPercentIndividualIncome() {
        return rankPercentIndividualIncome;
    }

    /** (Required) */
    public void setRankPercentIndividualIncome(Object rankPercentIndividualIncome) {
        this.rankPercentIndividualIncome = rankPercentIndividualIncome;
    }

    /** (Required) */
    public Object getPercentFemaleIncomeLessThan() {
        return percentFemaleIncomeLessThan;
    }

    /** (Required) */
    public void setPercentFemaleIncomeLessThan(Object percentFemaleIncomeLessThan) {
        this.percentFemaleIncomeLessThan = percentFemaleIncomeLessThan;
    }

    /** (Required) */
    public Object getRankPercentFemaleIncomeLess() {
        return rankPercentFemaleIncomeLess;
    }

    /** (Required) */
    public void setRankPercentFemaleIncomeLess(Object rankPercentFemaleIncomeLess) {
        this.rankPercentFemaleIncomeLess = rankPercentFemaleIncomeLess;
    }

    /** (Required) */
    public Object getPercentMaleIncomeLessThan4() {
        return percentMaleIncomeLessThan4;
    }

    /** (Required) */
    public void setPercentMaleIncomeLessThan4(Object percentMaleIncomeLessThan4) {
        this.percentMaleIncomeLessThan4 = percentMaleIncomeLessThan4;
    }

    /** (Required) */
    public Object getRankPercentMaleIncomeLessT() {
        return rankPercentMaleIncomeLessT;
    }

    /** (Required) */
    public void setRankPercentMaleIncomeLessT(Object rankPercentMaleIncomeLessT) {
        this.rankPercentMaleIncomeLessT = rankPercentMaleIncomeLessT;
    }

    /** (Required) */
    public Object getPercentOneParentHeadedFamil() {
        return percentOneParentHeadedFamil;
    }

    /** (Required) */
    public void setPercentOneParentHeadedFamil(Object percentOneParentHeadedFamil) {
        this.percentOneParentHeadedFamil = percentOneParentHeadedFamil;
    }

    /** (Required) */
    public Object getRankPercentOneParentHeaded() {
        return rankPercentOneParentHeaded;
    }

    /** (Required) */
    public void setRankPercentOneParentHeaded(Object rankPercentOneParentHeaded) {
        this.rankPercentOneParentHeaded = rankPercentOneParentHeaded;
    }

    /** (Required) */
    public Object getOneParentHeadedFamiliesPerc() {
        return oneParentHeadedFamiliesPerc;
    }

    /** (Required) */
    public void setOneParentHeadedFamiliesPerc(Object oneParentHeadedFamiliesPerc) {
        this.oneParentHeadedFamiliesPerc = oneParentHeadedFamiliesPerc;
    }

    /** (Required) */
    public Object getRankOneParentHeadedFamilies() {
        return rankOneParentHeadedFamilies;
    }

    /** (Required) */
    public void setRankOneParentHeadedFamilies(Object rankOneParentHeadedFamilies) {
        this.rankOneParentHeadedFamilies = rankOneParentHeadedFamilies;
    }

    /** (Required) */
    public Object getOneParentHeadedFamiliesPe1() {
        return oneParentHeadedFamiliesPe1;
    }

    /** (Required) */
    public void setOneParentHeadedFamiliesPe1(Object oneParentHeadedFamiliesPe1) {
        this.oneParentHeadedFamiliesPe1 = oneParentHeadedFamiliesPe1;
    }

    /** (Required) */
    public Object getRankOneParentHeadedFamili1() {
        return rankOneParentHeadedFamili1;
    }

    /** (Required) */
    public void setRankOneParentHeadedFamili1(Object rankOneParentHeadedFamili1) {
        this.rankOneParentHeadedFamili1 = rankOneParentHeadedFamili1;
    }

    /** (Required) */
    public Object getEquivalisedMedianIncome() {
        return equivalisedMedianIncome;
    }

    /** (Required) */
    public void setEquivalisedMedianIncome(Object equivalisedMedianIncome) {
        this.equivalisedMedianIncome = equivalisedMedianIncome;
    }

    /** (Required) */
    public Object getRankEquivalisedMedianIncome() {
        return rankEquivalisedMedianIncome;
    }

    /** (Required) */
    public void setRankEquivalisedMedianIncome(Object rankEquivalisedMedianIncome) {
        this.rankEquivalisedMedianIncome = rankEquivalisedMedianIncome;
    }

    /** (Required) */
    public Object getDelayedMedicalConsultationBe() {
        return delayedMedicalConsultationBe;
    }

    /** (Required) */
    public void setDelayedMedicalConsultationBe(Object delayedMedicalConsultationBe) {
        this.delayedMedicalConsultationBe = delayedMedicalConsultationBe;
    }

    /** (Required) */
    public Object getRankDelayedMedicalConsultati() {
        return rankDelayedMedicalConsultati;
    }

    /** (Required) */
    public void setRankDelayedMedicalConsultati(Object rankDelayedMedicalConsultati) {
        this.rankDelayedMedicalConsultati = rankDelayedMedicalConsultati;
    }

    /** (Required) */
    public Object getDelayedPurchasingPrescribedM() {
        return delayedPurchasingPrescribedM;
    }

    /** (Required) */
    public void setDelayedPurchasingPrescribedM(Object delayedPurchasingPrescribedM) {
        this.delayedPurchasingPrescribedM = delayedPurchasingPrescribedM;
    }

    /** (Required) */
    public Object getRankDelayedPurchasingPrescri() {
        return rankDelayedPurchasingPrescri;
    }

    /** (Required) */
    public void setRankDelayedPurchasingPrescri(Object rankDelayedPurchasingPrescri) {
        this.rankDelayedPurchasingPrescri = rankDelayedPurchasingPrescri;
    }

    /** (Required) */
    public Object getPercentLowIncomeWelfareDepe() {
        return percentLowIncomeWelfareDepe;
    }

    /** (Required) */
    public void setPercentLowIncomeWelfareDepe(Object percentLowIncomeWelfareDepe) {
        this.percentLowIncomeWelfareDepe = percentLowIncomeWelfareDepe;
    }

    /** (Required) */
    public Object getRankPercentLowIncomeWelfare() {
        return rankPercentLowIncomeWelfare;
    }

    /** (Required) */
    public void setRankPercentLowIncomeWelfare(Object rankPercentLowIncomeWelfare) {
        this.rankPercentLowIncomeWelfare = rankPercentLowIncomeWelfare;
    }

    /** (Required) */
    public Object getPercentOfPopWithFoodInsecu() {
        return percentOfPopWithFoodInsecu;
    }

    /** (Required) */
    public void setPercentOfPopWithFoodInsecu(Object percentOfPopWithFoodInsecu) {
        this.percentOfPopWithFoodInsecu = percentOfPopWithFoodInsecu;
    }

    /** (Required) */
    public Object getRankPercentOfPopWithFoodI() {
        return rankPercentOfPopWithFoodI;
    }

    /** (Required) */
    public void setRankPercentOfPopWithFoodI(Object rankPercentOfPopWithFoodI) {
        this.rankPercentOfPopWithFoodI = rankPercentOfPopWithFoodI;
    }

    /** (Required) */
    public Object getPercentMortgageStress() {
        return percentMortgageStress;
    }

    /** (Required) */
    public void setPercentMortgageStress(Object percentMortgageStress) {
        this.percentMortgageStress = percentMortgageStress;
    }

    /** (Required) */
    public Object getRankPercentMortgageStress() {
        return rankPercentMortgageStress;
    }

    /** (Required) */
    public void setRankPercentMortgageStress(Object rankPercentMortgageStress) {
        this.rankPercentMortgageStress = rankPercentMortgageStress;
    }

    /** (Required) */
    public Object getPercentRentalStress() {
        return percentRentalStress;
    }

    /** (Required) */
    public void setPercentRentalStress(Object percentRentalStress) {
        this.percentRentalStress = percentRentalStress;
    }

    /** (Required) */
    public Object getRankPercentRentalStress() {
        return rankPercentRentalStress;
    }

    /** (Required) */
    public void setRankPercentRentalStress(Object rankPercentRentalStress) {
        this.rankPercentRentalStress = rankPercentRentalStress;
    }

    /** (Required) */
    public Object getPercentOfRentalHousingThat() {
        return percentOfRentalHousingThat;
    }

    /** (Required) */
    public void setPercentOfRentalHousingThat(Object percentOfRentalHousingThat) {
        this.percentOfRentalHousingThat = percentOfRentalHousingThat;
    }

    /** (Required) */
    public Object getRankPercentOfRentalHousing() {
        return rankPercentOfRentalHousing;
    }

    /** (Required) */
    public void setRankPercentOfRentalHousing(Object rankPercentOfRentalHousing) {
        this.rankPercentOfRentalHousing = rankPercentOfRentalHousing;
    }

    /** (Required) */
    public Object getMedianHousePrice() {
        return medianHousePrice;
    }

    /** (Required) */
    public void setMedianHousePrice(Object medianHousePrice) {
        this.medianHousePrice = medianHousePrice;
    }

    /** (Required) */
    public Object getRankMedianHousePrice() {
        return rankMedianHousePrice;
    }

    /** (Required) */
    public void setRankMedianHousePrice(Object rankMedianHousePrice) {
        this.rankMedianHousePrice = rankMedianHousePrice;
    }

    /** (Required) */
    public Object getMedianRentFor3BedroomsHome() {
        return medianRentFor3BedroomsHome;
    }

    /** (Required) */
    public void setMedianRentFor3BedroomsHome(Object medianRentFor3BedroomsHome) {
        this.medianRentFor3BedroomsHome = medianRentFor3BedroomsHome;
    }

    /** (Required) */
    public Object getRankMedianRentFor3Bedrooms() {
        return rankMedianRentFor3Bedrooms;
    }

    /** (Required) */
    public void setRankMedianRentFor3Bedrooms(Object rankMedianRentFor3Bedrooms) {
        this.rankMedianRentFor3Bedrooms = rankMedianRentFor3Bedrooms;
    }

    /** (Required) */
    public Object getNewDwellingsApprovedForCons() {
        return newDwellingsApprovedForCons;
    }

    /** (Required) */
    public void setNewDwellingsApprovedForCons(Object newDwellingsApprovedForCons) {
        this.newDwellingsApprovedForCons = newDwellingsApprovedForCons;
    }

    /** (Required) */
    public Object getRankNewDwellingsApprovedFor() {
        return rankNewDwellingsApprovedFor;
    }

    /** (Required) */
    public void setRankNewDwellingsApprovedFor(Object rankNewDwellingsApprovedFor) {
        this.rankNewDwellingsApprovedFor = rankNewDwellingsApprovedFor;
    }

    /** (Required) */
    public Object getSocialHousingStockAsAPerce() {
        return socialHousingStockAsAPerce;
    }

    /** (Required) */
    public void setSocialHousingStockAsAPerce(Object socialHousingStockAsAPerce) {
        this.socialHousingStockAsAPerce = socialHousingStockAsAPerce;
    }

    /** (Required) */
    public Object getRankSocialHousingStockAsA() {
        return rankSocialHousingStockAsA;
    }

    /** (Required) */
    public void setRankSocialHousingStockAsA(Object rankSocialHousingStockAsA) {
        this.rankSocialHousingStockAsA = rankSocialHousingStockAsA;
    }

    /** (Required) */
    public Object getNumberOfSocialHousingDwelli() {
        return numberOfSocialHousingDwelli;
    }

    /** (Required) */
    public void setNumberOfSocialHousingDwelli(Object numberOfSocialHousingDwelli) {
        this.numberOfSocialHousingDwelli = numberOfSocialHousingDwelli;
    }

    /** (Required) */
    public Object getRankNumberOfSocialHousingD() {
        return rankNumberOfSocialHousingD;
    }

    /** (Required) */
    public void setRankNumberOfSocialHousingD(Object rankNumberOfSocialHousingD) {
        this.rankNumberOfSocialHousingD = rankNumberOfSocialHousingD;
    }

    /** (Required) */
    public Object getHomelessnessRatePer1000Pop() {
        return homelessnessRatePer1000Pop;
    }

    /** (Required) */
    public void setHomelessnessRatePer1000Pop(Object homelessnessRatePer1000Pop) {
        this.homelessnessRatePer1000Pop = homelessnessRatePer1000Pop;
    }

    /** (Required) */
    public Object getRankHomelessnessRatePer1000() {
        return rankHomelessnessRatePer1000;
    }

    /** (Required) */
    public void setRankHomelessnessRatePer1000(Object rankHomelessnessRatePer1000) {
        this.rankHomelessnessRatePer1000 = rankHomelessnessRatePer1000;
    }

    /** (Required) */
    public Object getPercentOfWorkJourneysWhich() {
        return percentOfWorkJourneysWhich;
    }

    /** (Required) */
    public void setPercentOfWorkJourneysWhich(Object percentOfWorkJourneysWhich) {
        this.percentOfWorkJourneysWhich = percentOfWorkJourneysWhich;
    }

    /** (Required) */
    public Object getRankPercentOfWorkJourneysW() {
        return rankPercentOfWorkJourneysW;
    }

    /** (Required) */
    public void setRankPercentOfWorkJourneysW(Object rankPercentOfWorkJourneysW) {
        this.rankPercentOfWorkJourneysW = rankPercentOfWorkJourneysW;
    }

    /** (Required) */
    public Object getPercentOfWorkJourneysWhich1() {
        return percentOfWorkJourneysWhich1;
    }

    /** (Required) */
    public void setPercentOfWorkJourneysWhich1(Object percentOfWorkJourneysWhich1) {
        this.percentOfWorkJourneysWhich1 = percentOfWorkJourneysWhich1;
    }

    /** (Required) */
    public Object getRankPercentOfWorkJourneys1() {
        return rankPercentOfWorkJourneys1;
    }

    /** (Required) */
    public void setRankPercentOfWorkJourneys1(Object rankPercentOfWorkJourneys1) {
        this.rankPercentOfWorkJourneys1 = rankPercentOfWorkJourneys1;
    }

    /** (Required) */
    public Object getPersonsWithAtLeast2HourDa() {
        return personsWithAtLeast2HourDa;
    }

    /** (Required) */
    public void setPersonsWithAtLeast2HourDa(Object personsWithAtLeast2HourDa) {
        this.personsWithAtLeast2HourDa = personsWithAtLeast2HourDa;
    }

    /** (Required) */
    public Object getRankPersonsWithAtLeast2Ho() {
        return rankPersonsWithAtLeast2Ho;
    }

    /** (Required) */
    public void setRankPersonsWithAtLeast2Ho(Object rankPersonsWithAtLeast2Ho) {
        this.rankPersonsWithAtLeast2Ho = rankPersonsWithAtLeast2Ho;
    }

    /** (Required) */
    public Object getPercentHouseholdsWithNoMoto() {
        return percentHouseholdsWithNoMoto;
    }

    /** (Required) */
    public void setPercentHouseholdsWithNoMoto(Object percentHouseholdsWithNoMoto) {
        this.percentHouseholdsWithNoMoto = percentHouseholdsWithNoMoto;
    }

    /** (Required) */
    public Object getRankPercentHouseholdsWithNo() {
        return rankPercentHouseholdsWithNo;
    }

    /** (Required) */
    public void setRankPercentHouseholdsWithNo(Object rankPercentHouseholdsWithNo) {
        this.rankPercentHouseholdsWithNo = rankPercentHouseholdsWithNo;
    }

    public Object getFTEStudents() {
        return fTEStudents;
    }

    public void setFTEStudents(Object fTEStudents) {
        this.fTEStudents = fTEStudents;
    }

    /** (Required) */
    public Object getPercentYear9StudentsWhoAtt() {
        return percentYear9StudentsWhoAtt;
    }

    /** (Required) */
    public void setPercentYear9StudentsWhoAtt(Object percentYear9StudentsWhoAtt) {
        this.percentYear9StudentsWhoAtt = percentYear9StudentsWhoAtt;
    }

    /** (Required) */
    public Object getRankPercentOfYear9Students() {
        return rankPercentOfYear9Students;
    }

    /** (Required) */
    public void setRankPercentOfYear9Students(Object rankPercentOfYear9Students) {
        this.rankPercentOfYear9Students = rankPercentOfYear9Students;
    }

    /** (Required) */
    public Object getPercentOfYear9StudentsWho() {
        return percentOfYear9StudentsWho;
    }

    /** (Required) */
    public void setPercentOfYear9StudentsWho(Object percentOfYear9StudentsWho) {
        this.percentOfYear9StudentsWho = percentOfYear9StudentsWho;
    }

    /** (Required) */
    public Object getRankPercentYear9StudentsWh() {
        return rankPercentYear9StudentsWh;
    }

    /** (Required) */
    public void setRankPercentYear9StudentsWh(Object rankPercentYear9StudentsWh) {
        this.rankPercentYear9StudentsWh = rankPercentYear9StudentsWh;
    }

    /** (Required) */
    public Object getPercent19YearOldsCompleting() {
        return percent19YearOldsCompleting;
    }

    /** (Required) */
    public void setPercent19YearOldsCompleting(Object percent19YearOldsCompleting) {
        this.percent19YearOldsCompleting = percent19YearOldsCompleting;
    }

    /** (Required) */
    public Object getRankPercent19YearOldsCompl() {
        return rankPercent19YearOldsCompl;
    }

    /** (Required) */
    public void setRankPercent19YearOldsCompl(Object rankPercent19YearOldsCompl) {
        this.rankPercent19YearOldsCompl = rankPercent19YearOldsCompl;
    }

    /** (Required) */
    public Object getPercentPersonsWhoDidNotCom() {
        return percentPersonsWhoDidNotCom;
    }

    /** (Required) */
    public void setPercentPersonsWhoDidNotCom(Object percentPersonsWhoDidNotCom) {
        this.percentPersonsWhoDidNotCom = percentPersonsWhoDidNotCom;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsWhoDid() {
        return rankPercentOfPersonsWhoDid;
    }

    /** (Required) */
    public void setRankPercentOfPersonsWhoDid(Object rankPercentOfPersonsWhoDid) {
        this.rankPercentOfPersonsWhoDid = rankPercentOfPersonsWhoDid;
    }

    /** (Required) */
    public Object getPercentPersonsWhoCompletedA() {
        return percentPersonsWhoCompletedA;
    }

    /** (Required) */
    public void setPercentPersonsWhoCompletedA(Object percentPersonsWhoCompletedA) {
        this.percentPersonsWhoCompletedA = percentPersonsWhoCompletedA;
    }

    /** (Required) */
    public Object getRankPercentPersonsWhoComple() {
        return rankPercentPersonsWhoComple;
    }

    /** (Required) */
    public void setRankPercentPersonsWhoComple(Object rankPercentPersonsWhoComple) {
        this.rankPercentPersonsWhoComple = rankPercentPersonsWhoComple;
    }

    /** (Required) */
    public Object getPercentOfSchoolChildrenAtte() {
        return percentOfSchoolChildrenAtte;
    }

    /** (Required) */
    public void setPercentOfSchoolChildrenAtte(Object percentOfSchoolChildrenAtte) {
        this.percentOfSchoolChildrenAtte = percentOfSchoolChildrenAtte;
    }

    /** (Required) */
    public Object getRankPercentOfSchoolChildren() {
        return rankPercentOfSchoolChildren;
    }

    /** (Required) */
    public void setRankPercentOfSchoolChildren(Object rankPercentOfSchoolChildren) {
        this.rankPercentOfSchoolChildren = rankPercentOfSchoolChildren;
    }

    /** (Required) */
    public Object getPercentOfPersonsReportingAs() {
        return percentOfPersonsReportingAs;
    }

    /** (Required) */
    public void setPercentOfPersonsReportingAs(Object percentOfPersonsReportingAs) {
        this.percentOfPersonsReportingAs = percentOfPersonsReportingAs;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsReporti() {
        return rankPercentOfPersonsReporti;
    }

    /** (Required) */
    public void setRankPercentOfPersonsReporti(Object rankPercentOfPersonsReporti) {
        this.rankPercentOfPersonsReporti = rankPercentOfPersonsReporti;
    }

    /** (Required) */
    public Object getPercentOfPersonsReportingTy() {
        return percentOfPersonsReportingTy;
    }

    /** (Required) */
    public void setPercentOfPersonsReportingTy(Object percentOfPersonsReportingTy) {
        this.percentOfPersonsReportingTy = percentOfPersonsReportingTy;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsRepor1() {
        return rankPercentOfPersonsRepor1;
    }

    /** (Required) */
    public void setRankPercentOfPersonsRepor1(Object rankPercentOfPersonsRepor1) {
        this.rankPercentOfPersonsRepor1 = rankPercentOfPersonsRepor1;
    }

    /** (Required) */
    public Object getPercentOfPersonsReportingHi() {
        return percentOfPersonsReportingHi;
    }

    /** (Required) */
    public void setPercentOfPersonsReportingHi(Object percentOfPersonsReportingHi) {
        this.percentOfPersonsReportingHi = percentOfPersonsReportingHi;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsRepor2() {
        return rankPercentOfPersonsRepor2;
    }

    /** (Required) */
    public void setRankPercentOfPersonsRepor2(Object rankPercentOfPersonsRepor2) {
        this.rankPercentOfPersonsRepor2 = rankPercentOfPersonsRepor2;
    }

    /** (Required) */
    public Object getPercentOfPersonsReportingHe() {
        return percentOfPersonsReportingHe;
    }

    /** (Required) */
    public void setPercentOfPersonsReportingHe(Object percentOfPersonsReportingHe) {
        this.percentOfPersonsReportingHe = percentOfPersonsReportingHe;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsRepor3() {
        return rankPercentOfPersonsRepor3;
    }

    /** (Required) */
    public void setRankPercentOfPersonsRepor3(Object rankPercentOfPersonsRepor3) {
        this.rankPercentOfPersonsRepor3 = rankPercentOfPersonsRepor3;
    }

    /** (Required) */
    public Object getPercentOfPersonsReportingOs() {
        return percentOfPersonsReportingOs;
    }

    /** (Required) */
    public void setPercentOfPersonsReportingOs(Object percentOfPersonsReportingOs) {
        this.percentOfPersonsReportingOs = percentOfPersonsReportingOs;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsRepor4() {
        return rankPercentOfPersonsRepor4;
    }

    /** (Required) */
    public void setRankPercentOfPersonsRepor4(Object rankPercentOfPersonsRepor4) {
        this.rankPercentOfPersonsRepor4 = rankPercentOfPersonsRepor4;
    }

    /** (Required) */
    public Object getPercentOfPersonsReportingAr() {
        return percentOfPersonsReportingAr;
    }

    /** (Required) */
    public void setPercentOfPersonsReportingAr(Object percentOfPersonsReportingAr) {
        this.percentOfPersonsReportingAr = percentOfPersonsReportingAr;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsRepor5() {
        return rankPercentOfPersonsRepor5;
    }

    /** (Required) */
    public void setRankPercentOfPersonsRepor5(Object rankPercentOfPersonsRepor5) {
        this.rankPercentOfPersonsRepor5 = rankPercentOfPersonsRepor5;
    }

    /** (Required) */
    public Object getPercentOfPersonsWhoAreOver() {
        return percentOfPersonsWhoAreOver;
    }

    /** (Required) */
    public void setPercentOfPersonsWhoAreOver(Object percentOfPersonsWhoAreOver) {
        this.percentOfPersonsWhoAreOver = percentOfPersonsWhoAreOver;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsWhoAre() {
        return rankPercentOfPersonsWhoAre;
    }

    /** (Required) */
    public void setRankPercentOfPersonsWhoAre(Object rankPercentOfPersonsWhoAre) {
        this.rankPercentOfPersonsWhoAre = rankPercentOfPersonsWhoAre;
    }

    /** (Required) */
    public Object getPercentOfFemalesWhoAreOver() {
        return percentOfFemalesWhoAreOver;
    }

    /** (Required) */
    public void setPercentOfFemalesWhoAreOver(Object percentOfFemalesWhoAreOver) {
        this.percentOfFemalesWhoAreOver = percentOfFemalesWhoAreOver;
    }

    /** (Required) */
    public Object getRankPercentOfFemalesWhoAre() {
        return rankPercentOfFemalesWhoAre;
    }

    /** (Required) */
    public void setRankPercentOfFemalesWhoAre(Object rankPercentOfFemalesWhoAre) {
        this.rankPercentOfFemalesWhoAre = rankPercentOfFemalesWhoAre;
    }

    /** (Required) */
    public Object getPercentOfMalesWhoAreOverwe() {
        return percentOfMalesWhoAreOverwe;
    }

    /** (Required) */
    public void setPercentOfMalesWhoAreOverwe(Object percentOfMalesWhoAreOverwe) {
        this.percentOfMalesWhoAreOverwe = percentOfMalesWhoAreOverwe;
    }

    /** (Required) */
    public Object getRankPercentOfMalesWhoAreO() {
        return rankPercentOfMalesWhoAreO;
    }

    /** (Required) */
    public void setRankPercentOfMalesWhoAreO(Object rankPercentOfMalesWhoAreO) {
        this.rankPercentOfMalesWhoAreO = rankPercentOfMalesWhoAreO;
    }

    /** (Required) */
    public Object getPercentOfPersonsWhoAreObes() {
        return percentOfPersonsWhoAreObes;
    }

    /** (Required) */
    public void setPercentOfPersonsWhoAreObes(Object percentOfPersonsWhoAreObes) {
        this.percentOfPersonsWhoAreObes = percentOfPersonsWhoAreObes;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsWhoA1() {
        return rankPercentOfPersonsWhoA1;
    }

    /** (Required) */
    public void setRankPercentOfPersonsWhoA1(Object rankPercentOfPersonsWhoA1) {
        this.rankPercentOfPersonsWhoA1 = rankPercentOfPersonsWhoA1;
    }

    /** (Required) */
    public Object getPercentOfFemalesWhoAreObes() {
        return percentOfFemalesWhoAreObes;
    }

    /** (Required) */
    public void setPercentOfFemalesWhoAreObes(Object percentOfFemalesWhoAreObes) {
        this.percentOfFemalesWhoAreObes = percentOfFemalesWhoAreObes;
    }

    /** (Required) */
    public Object getRankPercentOfFemalesWhoA1() {
        return rankPercentOfFemalesWhoA1;
    }

    /** (Required) */
    public void setRankPercentOfFemalesWhoA1(Object rankPercentOfFemalesWhoA1) {
        this.rankPercentOfFemalesWhoA1 = rankPercentOfFemalesWhoA1;
    }

    /** (Required) */
    public Object getPercentOfMalesWhoAreObese() {
        return percentOfMalesWhoAreObese;
    }

    /** (Required) */
    public void setPercentOfMalesWhoAreObese(Object percentOfMalesWhoAreObese) {
        this.percentOfMalesWhoAreObese = percentOfMalesWhoAreObese;
    }

    /** (Required) */
    public Object getRankPercentOfMalesWhoAre1() {
        return rankPercentOfMalesWhoAre1;
    }

    /** (Required) */
    public void setRankPercentOfMalesWhoAre1(Object rankPercentOfMalesWhoAre1) {
        this.rankPercentOfMalesWhoAre1 = rankPercentOfMalesWhoAre1;
    }

    /** (Required) */
    public Object getMalignantCancersDiagnosedPer() {
        return malignantCancersDiagnosedPer;
    }

    /** (Required) */
    public void setMalignantCancersDiagnosedPer(Object malignantCancersDiagnosedPer) {
        this.malignantCancersDiagnosedPer = malignantCancersDiagnosedPer;
    }

    /** (Required) */
    public Object getRankMalignantCancersDiagnose() {
        return rankMalignantCancersDiagnose;
    }

    /** (Required) */
    public void setRankMalignantCancersDiagnose(Object rankMalignantCancersDiagnose) {
        this.rankMalignantCancersDiagnose = rankMalignantCancersDiagnose;
    }

    /** (Required) */
    public Object getMaleCancerIncidencePer1000() {
        return maleCancerIncidencePer1000;
    }

    /** (Required) */
    public void setMaleCancerIncidencePer1000(Object maleCancerIncidencePer1000) {
        this.maleCancerIncidencePer1000 = maleCancerIncidencePer1000;
    }

    /** (Required) */
    public Object getRankMaleCancerIncidencePer() {
        return rankMaleCancerIncidencePer;
    }

    /** (Required) */
    public void setRankMaleCancerIncidencePer(Object rankMaleCancerIncidencePer) {
        this.rankMaleCancerIncidencePer = rankMaleCancerIncidencePer;
    }

    /** (Required) */
    public Object getFemaleCancerIncidencePer100() {
        return femaleCancerIncidencePer100;
    }

    /** (Required) */
    public void setFemaleCancerIncidencePer100(Object femaleCancerIncidencePer100) {
        this.femaleCancerIncidencePer100 = femaleCancerIncidencePer100;
    }

    /** (Required) */
    public Object getRankFemaleCancerIncidencePe() {
        return rankFemaleCancerIncidencePe;
    }

    /** (Required) */
    public void setRankFemaleCancerIncidencePe(Object rankFemaleCancerIncidencePe) {
        this.rankFemaleCancerIncidencePe = rankFemaleCancerIncidencePe;
    }

    /** (Required) */
    public Object getPercentPoorDentalHealth() {
        return percentPoorDentalHealth;
    }

    /** (Required) */
    public void setPercentPoorDentalHealth(Object percentPoorDentalHealth) {
        this.percentPoorDentalHealth = percentPoorDentalHealth;
    }

    /** (Required) */
    public Object getRankPercentPoorDentalHealth() {
        return rankPercentPoorDentalHealth;
    }

    /** (Required) */
    public void setRankPercentPoorDentalHealth(Object rankPercentPoorDentalHealth) {
        this.rankPercentPoorDentalHealth = rankPercentPoorDentalHealth;
    }

    /** (Required) */
    public Object getNotificationsPer100000PopOf() {
        return notificationsPer100000PopOf;
    }

    /** (Required) */
    public void setNotificationsPer100000PopOf(Object notificationsPer100000PopOf) {
        this.notificationsPer100000PopOf = notificationsPer100000PopOf;
    }

    /** (Required) */
    public Object getRankNotificationsPer100000P() {
        return rankNotificationsPer100000P;
    }

    /** (Required) */
    public void setRankNotificationsPer100000P(Object rankNotificationsPer100000P) {
        this.rankNotificationsPer100000P = rankNotificationsPer100000P;
    }

    /** (Required) */
    public Object getNotificationsPer100000Pop1() {
        return notificationsPer100000Pop1;
    }

    /** (Required) */
    public void setNotificationsPer100000Pop1(Object notificationsPer100000Pop1) {
        this.notificationsPer100000Pop1 = notificationsPer100000Pop1;
    }

    /** (Required) */
    public Object getRankNotificationsPer1000001() {
        return rankNotificationsPer1000001;
    }

    /** (Required) */
    public void setRankNotificationsPer1000001(Object rankNotificationsPer1000001) {
        this.rankNotificationsPer1000001 = rankNotificationsPer1000001;
    }

    /** (Required) */
    public Object getNotificationsPer100000People() {
        return notificationsPer100000People;
    }

    /** (Required) */
    public void setNotificationsPer100000People(Object notificationsPer100000People) {
        this.notificationsPer100000People = notificationsPer100000People;
    }

    /** (Required) */
    public Object getRankNotificationsPer1000002() {
        return rankNotificationsPer1000002;
    }

    /** (Required) */
    public void setRankNotificationsPer1000002(Object rankNotificationsPer1000002) {
        this.rankNotificationsPer1000002 = rankNotificationsPer1000002;
    }

    /** (Required) */
    public Object getPercentOfPersons18yrsPlusWh() {
        return percentOfPersons18yrsPlusWh;
    }

    /** (Required) */
    public void setPercentOfPersons18yrsPlusWh(Object percentOfPersons18yrsPlusWh) {
        this.percentOfPersons18yrsPlusWh = percentOfPersons18yrsPlusWh;
    }

    /** (Required) */
    public Object getRankPercentOfPersons18yrsPl() {
        return rankPercentOfPersons18yrsPl;
    }

    /** (Required) */
    public void setRankPercentOfPersons18yrsPl(Object rankPercentOfPersons18yrsPl) {
        this.rankPercentOfPersons18yrsPl = rankPercentOfPersons18yrsPl;
    }

    /** (Required) */
    public Object getPercentOfMales18yrsPlusWho() {
        return percentOfMales18yrsPlusWho;
    }

    /** (Required) */
    public void setPercentOfMales18yrsPlusWho(Object percentOfMales18yrsPlusWho) {
        this.percentOfMales18yrsPlusWho = percentOfMales18yrsPlusWho;
    }

    /** (Required) */
    public Object getRankPercentOfMales18yrsPlus() {
        return rankPercentOfMales18yrsPlus;
    }

    /** (Required) */
    public void setRankPercentOfMales18yrsPlus(Object rankPercentOfMales18yrsPlus) {
        this.rankPercentOfMales18yrsPlus = rankPercentOfMales18yrsPlus;
    }

    /** (Required) */
    public Object getPercentOfFemales18yrsPlusWh() {
        return percentOfFemales18yrsPlusWh;
    }

    /** (Required) */
    public void setPercentOfFemales18yrsPlusWh(Object percentOfFemales18yrsPlusWh) {
        this.percentOfFemales18yrsPlusWh = percentOfFemales18yrsPlusWh;
    }

    /** (Required) */
    public Object getRankPercentOfFemales18yrsPl() {
        return rankPercentOfFemales18yrsPl;
    }

    /** (Required) */
    public void setRankPercentOfFemales18yrsPl(Object rankPercentOfFemales18yrsPl) {
        this.rankPercentOfFemales18yrsPl = rankPercentOfFemales18yrsPl;
    }

    /** (Required) */
    public Object getConsumedAlcoholAtLeastWeekl() {
        return consumedAlcoholAtLeastWeekl;
    }

    /** (Required) */
    public void setConsumedAlcoholAtLeastWeekl(Object consumedAlcoholAtLeastWeekl) {
        this.consumedAlcoholAtLeastWeekl = consumedAlcoholAtLeastWeekl;
    }

    /** (Required) */
    public Object getRankConsumedAlcoholAtLeast() {
        return rankConsumedAlcoholAtLeast;
    }

    /** (Required) */
    public void setRankConsumedAlcoholAtLeast(Object rankConsumedAlcoholAtLeast) {
        this.rankConsumedAlcoholAtLeast = rankConsumedAlcoholAtLeast;
    }

    /** (Required) */
    public Object getConsumedAlcoholAtLeastWee1() {
        return consumedAlcoholAtLeastWee1;
    }

    /** (Required) */
    public void setConsumedAlcoholAtLeastWee1(Object consumedAlcoholAtLeastWee1) {
        this.consumedAlcoholAtLeastWee1 = consumedAlcoholAtLeastWee1;
    }

    /** (Required) */
    public Object getRankConsumedAlcoholAtLeast1() {
        return rankConsumedAlcoholAtLeast1;
    }

    /** (Required) */
    public void setRankConsumedAlcoholAtLeast1(Object rankConsumedAlcoholAtLeast1) {
        this.rankConsumedAlcoholAtLeast1 = rankConsumedAlcoholAtLeast1;
    }

    /** (Required) */
    public Object getPercentOfPersonsWhoDoNotM() {
        return percentOfPersonsWhoDoNotM;
    }

    /** (Required) */
    public void setPercentOfPersonsWhoDoNotM(Object percentOfPersonsWhoDoNotM) {
        this.percentOfPersonsWhoDoNotM = percentOfPersonsWhoDoNotM;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsWhoDo() {
        return rankPercentOfPersonsWhoDo;
    }

    /** (Required) */
    public void setRankPercentOfPersonsWhoDo(Object rankPercentOfPersonsWhoDo) {
        this.rankPercentOfPersonsWhoDo = rankPercentOfPersonsWhoDo;
    }

    /** (Required) */
    public Object getPercentOfMalesWhoDoNotMee() {
        return percentOfMalesWhoDoNotMee;
    }

    /** (Required) */
    public void setPercentOfMalesWhoDoNotMee(Object percentOfMalesWhoDoNotMee) {
        this.percentOfMalesWhoDoNotMee = percentOfMalesWhoDoNotMee;
    }

    /** (Required) */
    public Object getRankPercentOfMalesWhoDoNo() {
        return rankPercentOfMalesWhoDoNo;
    }

    /** (Required) */
    public void setRankPercentOfMalesWhoDoNo(Object rankPercentOfMalesWhoDoNo) {
        this.rankPercentOfMalesWhoDoNo = rankPercentOfMalesWhoDoNo;
    }

    /** (Required) */
    public Object getPercentOfFemalesWhoDoNotM() {
        return percentOfFemalesWhoDoNotM;
    }

    /** (Required) */
    public void setPercentOfFemalesWhoDoNotM(Object percentOfFemalesWhoDoNotM) {
        this.percentOfFemalesWhoDoNotM = percentOfFemalesWhoDoNotM;
    }

    /** (Required) */
    public Object getRankPercentOfFemalesWhoDo() {
        return rankPercentOfFemalesWhoDo;
    }

    /** (Required) */
    public void setRankPercentOfFemalesWhoDo(Object rankPercentOfFemalesWhoDo) {
        this.rankPercentOfFemalesWhoDo = rankPercentOfFemalesWhoDo;
    }

    /** (Required) */
    public Object getPercentOfPersonsWhoDrinkSo() {
        return percentOfPersonsWhoDrinkSo;
    }

    /** (Required) */
    public void setPercentOfPersonsWhoDrinkSo(Object percentOfPersonsWhoDrinkSo) {
        this.percentOfPersonsWhoDrinkSo = percentOfPersonsWhoDrinkSo;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsWhoDri() {
        return rankPercentOfPersonsWhoDri;
    }

    /** (Required) */
    public void setRankPercentOfPersonsWhoDri(Object rankPercentOfPersonsWhoDri) {
        this.rankPercentOfPersonsWhoDri = rankPercentOfPersonsWhoDri;
    }

    /** (Required) */
    public Object getPercentOfPersonsWhoShareA() {
        return percentOfPersonsWhoShareA;
    }

    /** (Required) */
    public void setPercentOfPersonsWhoShareA(Object percentOfPersonsWhoShareA) {
        this.percentOfPersonsWhoShareA = percentOfPersonsWhoShareA;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsWhoSha() {
        return rankPercentOfPersonsWhoSha;
    }

    /** (Required) */
    public void setRankPercentOfPersonsWhoSha(Object rankPercentOfPersonsWhoSha) {
        this.rankPercentOfPersonsWhoSha = rankPercentOfPersonsWhoSha;
    }

    /** (Required) */
    public Object getPercentOfPersonsWhoDoNot1() {
        return percentOfPersonsWhoDoNot1;
    }

    /** (Required) */
    public void setPercentOfPersonsWhoDoNot1(Object percentOfPersonsWhoDoNot1) {
        this.percentOfPersonsWhoDoNot1 = percentOfPersonsWhoDoNot1;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsWhoDo1() {
        return rankPercentOfPersonsWhoDo1;
    }

    /** (Required) */
    public void setRankPercentOfPersonsWhoDo1(Object rankPercentOfPersonsWhoDo1) {
        this.rankPercentOfPersonsWhoDo1 = rankPercentOfPersonsWhoDo1;
    }

    /** (Required) */
    public Object getPercentOfMalesWhoDoNotM1() {
        return percentOfMalesWhoDoNotM1;
    }

    /** (Required) */
    public void setPercentOfMalesWhoDoNotM1(Object percentOfMalesWhoDoNotM1) {
        this.percentOfMalesWhoDoNotM1 = percentOfMalesWhoDoNotM1;
    }

    /** (Required) */
    public Object getRankPercentOfMalesWhoDo1() {
        return rankPercentOfMalesWhoDo1;
    }

    /** (Required) */
    public void setRankPercentOfMalesWhoDo1(Object rankPercentOfMalesWhoDo1) {
        this.rankPercentOfMalesWhoDo1 = rankPercentOfMalesWhoDo1;
    }

    /** (Required) */
    public Object getPercentOfFemalesWhoDoNot1() {
        return percentOfFemalesWhoDoNot1;
    }

    /** (Required) */
    public void setPercentOfFemalesWhoDoNot1(Object percentOfFemalesWhoDoNot1) {
        this.percentOfFemalesWhoDoNot1 = percentOfFemalesWhoDoNot1;
    }

    /** (Required) */
    public Object getRankPercentOfFemalesWhoDo1() {
        return rankPercentOfFemalesWhoDo1;
    }

    /** (Required) */
    public void setRankPercentOfFemalesWhoDo1(Object rankPercentOfFemalesWhoDo1) {
        this.rankPercentOfFemalesWhoDo1 = rankPercentOfFemalesWhoDo1;
    }

    /** (Required) */
    public Object getPercentOfPersonsWhoSitFor() {
        return percentOfPersonsWhoSitFor;
    }

    /** (Required) */
    public void setPercentOfPersonsWhoSitFor(Object percentOfPersonsWhoSitFor) {
        this.percentOfPersonsWhoSitFor = percentOfPersonsWhoSitFor;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsWhoSit() {
        return rankPercentOfPersonsWhoSit;
    }

    /** (Required) */
    public void setRankPercentOfPersonsWhoSit(Object rankPercentOfPersonsWhoSit) {
        this.rankPercentOfPersonsWhoSit = rankPercentOfPersonsWhoSit;
    }

    /** (Required) */
    public Object getPercentOfPersonsWhoVisitA() {
        return percentOfPersonsWhoVisitA;
    }

    /** (Required) */
    public void setPercentOfPersonsWhoVisitA(Object percentOfPersonsWhoVisitA) {
        this.percentOfPersonsWhoVisitA = percentOfPersonsWhoVisitA;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsWhoVis() {
        return rankPercentOfPersonsWhoVis;
    }

    /** (Required) */
    public void setRankPercentOfPersonsWhoVis(Object rankPercentOfPersonsWhoVis) {
        this.rankPercentOfPersonsWhoVis = rankPercentOfPersonsWhoVis;
    }

    /** (Required) */
    public Object getPercentOfBreastScreeningPar() {
        return percentOfBreastScreeningPar;
    }

    /** (Required) */
    public void setPercentOfBreastScreeningPar(Object percentOfBreastScreeningPar) {
        this.percentOfBreastScreeningPar = percentOfBreastScreeningPar;
    }

    /** (Required) */
    public Object getRankPercentOfBreastScreenin() {
        return rankPercentOfBreastScreenin;
    }

    /** (Required) */
    public void setRankPercentOfBreastScreenin(Object rankPercentOfBreastScreenin) {
        this.rankPercentOfBreastScreenin = rankPercentOfBreastScreenin;
    }

    /** (Required) */
    public Object getPercentOfCervicalCancerScre() {
        return percentOfCervicalCancerScre;
    }

    /** (Required) */
    public void setPercentOfCervicalCancerScre(Object percentOfCervicalCancerScre) {
        this.percentOfCervicalCancerScre = percentOfCervicalCancerScre;
    }

    /** (Required) */
    public Object getRankPercentOfCervicalCancer() {
        return rankPercentOfCervicalCancer;
    }

    /** (Required) */
    public void setRankPercentOfCervicalCancer(Object rankPercentOfCervicalCancer) {
        this.rankPercentOfCervicalCancer = rankPercentOfCervicalCancer;
    }

    /** (Required) */
    public Object getBowelCancerScreeningParticip() {
        return bowelCancerScreeningParticip;
    }

    /** (Required) */
    public void setBowelCancerScreeningParticip(Object bowelCancerScreeningParticip) {
        this.bowelCancerScreeningParticip = bowelCancerScreeningParticip;
    }

    /** (Required) */
    public Object getRankBowelCancerScreeningPar() {
        return rankBowelCancerScreeningPar;
    }

    /** (Required) */
    public void setRankBowelCancerScreeningPar(Object rankBowelCancerScreeningPar) {
        this.rankBowelCancerScreeningPar = rankBowelCancerScreeningPar;
    }

    /** (Required) */
    public Object getBowelCancerScreeningPartic1() {
        return bowelCancerScreeningPartic1;
    }

    /** (Required) */
    public void setBowelCancerScreeningPartic1(Object bowelCancerScreeningPartic1) {
        this.bowelCancerScreeningPartic1 = bowelCancerScreeningPartic1;
    }

    /** (Required) */
    public Object getRankBowelCancerScreeningP1() {
        return rankBowelCancerScreeningP1;
    }

    /** (Required) */
    public void setRankBowelCancerScreeningP1(Object rankBowelCancerScreeningP1) {
        this.rankBowelCancerScreeningP1 = rankBowelCancerScreeningP1;
    }

    /** (Required) */
    public Object getBowelCancerScreeningPartic2() {
        return bowelCancerScreeningPartic2;
    }

    /** (Required) */
    public void setBowelCancerScreeningPartic2(Object bowelCancerScreeningPartic2) {
        this.bowelCancerScreeningPartic2 = bowelCancerScreeningPartic2;
    }

    /** (Required) */
    public Object getRankBowelCancerScreeningP2() {
        return rankBowelCancerScreeningP2;
    }

    /** (Required) */
    public void setRankBowelCancerScreeningP2(Object rankBowelCancerScreeningP2) {
        this.rankBowelCancerScreeningP2 = rankBowelCancerScreeningP2;
    }

    /** (Required) */
    public Object getLowBirthweightBabies() {
        return lowBirthweightBabies;
    }

    /** (Required) */
    public void setLowBirthweightBabies(Object lowBirthweightBabies) {
        this.lowBirthweightBabies = lowBirthweightBabies;
    }

    /** (Required) */
    public Object getRankLowBirthweightBabies() {
        return rankLowBirthweightBabies;
    }

    /** (Required) */
    public void setRankLowBirthweightBabies(Object rankLowBirthweightBabies) {
        this.rankLowBirthweightBabies = rankLowBirthweightBabies;
    }

    /** (Required) */
    public Object getPercentInfantsFullyBreastfed() {
        return percentInfantsFullyBreastfed;
    }

    /** (Required) */
    public void setPercentInfantsFullyBreastfed(Object percentInfantsFullyBreastfed) {
        this.percentInfantsFullyBreastfed = percentInfantsFullyBreastfed;
    }

    /** (Required) */
    public Object getRankPercentInfantsFullyBrea() {
        return rankPercentInfantsFullyBrea;
    }

    /** (Required) */
    public void setRankPercentInfantsFullyBrea(Object rankPercentInfantsFullyBrea) {
        this.rankPercentInfantsFullyBrea = rankPercentInfantsFullyBrea;
    }

    /** (Required) */
    public Object getPercentChildrenFullyImmunise() {
        return percentChildrenFullyImmunise;
    }

    /** (Required) */
    public void setPercentChildrenFullyImmunise(Object percentChildrenFullyImmunise) {
        this.percentChildrenFullyImmunise = percentChildrenFullyImmunise;
    }

    /** (Required) */
    public Object getRankPercentChildrenFullyImm() {
        return rankPercentChildrenFullyImm;
    }

    /** (Required) */
    public void setRankPercentChildrenFullyImm(Object rankPercentChildrenFullyImm) {
        this.rankPercentChildrenFullyImm = rankPercentChildrenFullyImm;
    }

    /** (Required) */
    public Object getProportionOfInfantsEnrolled() {
        return proportionOfInfantsEnrolled;
    }

    /** (Required) */
    public void setProportionOfInfantsEnrolled(Object proportionOfInfantsEnrolled) {
        this.proportionOfInfantsEnrolled = proportionOfInfantsEnrolled;
    }

    /** (Required) */
    public Object getRankProportionOfInfantsEnro() {
        return rankProportionOfInfantsEnro;
    }

    /** (Required) */
    public void setRankProportionOfInfantsEnro(Object rankProportionOfInfantsEnro) {
        this.rankProportionOfInfantsEnro = rankProportionOfInfantsEnro;
    }

    /** (Required) */
    public Object getKindergartenParticipationRate() {
        return kindergartenParticipationRate;
    }

    /** (Required) */
    public void setKindergartenParticipationRate(Object kindergartenParticipationRate) {
        this.kindergartenParticipationRate = kindergartenParticipationRate;
    }

    /** (Required) */
    public Object getPercentOfChildrenWithKinder() {
        return percentOfChildrenWithKinder;
    }

    /** (Required) */
    public void setPercentOfChildrenWithKinder(Object percentOfChildrenWithKinder) {
        this.percentOfChildrenWithKinder = percentOfChildrenWithKinder;
    }

    /** (Required) */
    public Object getRankPercentOfChildrenWithK() {
        return rankPercentOfChildrenWithK;
    }

    /** (Required) */
    public void setRankPercentOfChildrenWithK(Object rankPercentOfChildrenWithK) {
        this.rankPercentOfChildrenWithK = rankPercentOfChildrenWithK;
    }

    /** (Required) */
    public Object getPercentOfChildrenWithEmotio() {
        return percentOfChildrenWithEmotio;
    }

    /** (Required) */
    public void setPercentOfChildrenWithEmotio(Object percentOfChildrenWithEmotio) {
        this.percentOfChildrenWithEmotio = percentOfChildrenWithEmotio;
    }

    /** (Required) */
    public Object getRankPercentOfChildrenWithE() {
        return rankPercentOfChildrenWithE;
    }

    /** (Required) */
    public void setRankPercentOfChildrenWithE(Object rankPercentOfChildrenWithE) {
        this.rankPercentOfChildrenWithE = rankPercentOfChildrenWithE;
    }

    /** (Required) */
    public Object getPercentOfChildrenWithSpeech() {
        return percentOfChildrenWithSpeech;
    }

    /** (Required) */
    public void setPercentOfChildrenWithSpeech(Object percentOfChildrenWithSpeech) {
        this.percentOfChildrenWithSpeech = percentOfChildrenWithSpeech;
    }

    /** (Required) */
    public Object getRankPercentOfChildrenWithS() {
        return rankPercentOfChildrenWithS;
    }

    /** (Required) */
    public void setRankPercentOfChildrenWithS(Object rankPercentOfChildrenWithS) {
        this.rankPercentOfChildrenWithS = rankPercentOfChildrenWithS;
    }

    /** (Required) */
    public Object getPercentOfAdolescentsWhoRepo() {
        return percentOfAdolescentsWhoRepo;
    }

    /** (Required) */
    public void setPercentOfAdolescentsWhoRepo(Object percentOfAdolescentsWhoRepo) {
        this.percentOfAdolescentsWhoRepo = percentOfAdolescentsWhoRepo;
    }

    /** (Required) */
    public Object getRankPercentOfAdolescentsWho() {
        return rankPercentOfAdolescentsWho;
    }

    /** (Required) */
    public void setRankPercentOfAdolescentsWho(Object rankPercentOfAdolescentsWho) {
        this.rankPercentOfAdolescentsWho = rankPercentOfAdolescentsWho;
    }

    /** (Required) */
    public Object getPercentOfChildrenWhoAreDev() {
        return percentOfChildrenWhoAreDev;
    }

    /** (Required) */
    public void setPercentOfChildrenWhoAreDev(Object percentOfChildrenWhoAreDev) {
        this.percentOfChildrenWhoAreDev = percentOfChildrenWhoAreDev;
    }

    /** (Required) */
    public Object getRankPercentOfChildrenWhoAr() {
        return rankPercentOfChildrenWhoAr;
    }

    /** (Required) */
    public void setRankPercentOfChildrenWhoAr(Object rankPercentOfChildrenWhoAr) {
        this.rankPercentOfChildrenWhoAr = rankPercentOfChildrenWhoAr;
    }

    /** (Required) */
    public Object getPercentOfChildrenWhoAreD1() {
        return percentOfChildrenWhoAreD1;
    }

    /** (Required) */
    public void setPercentOfChildrenWhoAreD1(Object percentOfChildrenWhoAreD1) {
        this.percentOfChildrenWhoAreD1 = percentOfChildrenWhoAreD1;
    }

    /** (Required) */
    public Object getRankPercentOfChildrenWho1() {
        return rankPercentOfChildrenWho1;
    }

    /** (Required) */
    public void setRankPercentOfChildrenWho1(Object rankPercentOfChildrenWho1) {
        this.rankPercentOfChildrenWho1 = rankPercentOfChildrenWho1;
    }

    /** (Required) */
    public Object getCoreActivityNeedForAssistan() {
        return coreActivityNeedForAssistan;
    }

    /** (Required) */
    public void setCoreActivityNeedForAssistan(Object coreActivityNeedForAssistan) {
        this.coreActivityNeedForAssistan = coreActivityNeedForAssistan;
    }

    /** (Required) */
    public Object getRankCoreActivityNeedForAss() {
        return rankCoreActivityNeedForAss;
    }

    /** (Required) */
    public void setRankCoreActivityNeedForAss(Object rankCoreActivityNeedForAss) {
        this.rankCoreActivityNeedForAss = rankCoreActivityNeedForAss;
    }

    /** (Required) */
    public Object getPeopleWithSevereAndProfound() {
        return peopleWithSevereAndProfound;
    }

    /** (Required) */
    public void setPeopleWithSevereAndProfound(Object peopleWithSevereAndProfound) {
        this.peopleWithSevereAndProfound = peopleWithSevereAndProfound;
    }

    /** (Required) */
    public Object getRankPeopleWithSevereAndPro() {
        return rankPeopleWithSevereAndPro;
    }

    /** (Required) */
    public void setRankPeopleWithSevereAndPro(Object rankPeopleWithSevereAndPro) {
        this.rankPeopleWithSevereAndPro = rankPeopleWithSevereAndPro;
    }

    /** (Required) */
    public Object getPeopleWithSevereAndProfou1() {
        return peopleWithSevereAndProfou1;
    }

    /** (Required) */
    public void setPeopleWithSevereAndProfou1(Object peopleWithSevereAndProfou1) {
        this.peopleWithSevereAndProfou1 = peopleWithSevereAndProfou1;
    }

    /** (Required) */
    public Object getRankPeopleWithSevereAndP1() {
        return rankPeopleWithSevereAndP1;
    }

    /** (Required) */
    public void setRankPeopleWithSevereAndP1(Object rankPeopleWithSevereAndP1) {
        this.rankPeopleWithSevereAndP1 = rankPeopleWithSevereAndP1;
    }

    /** (Required) */
    public Object getPercentPopAged75yrsPlusLivi() {
        return percentPopAged75yrsPlusLivi;
    }

    /** (Required) */
    public void setPercentPopAged75yrsPlusLivi(Object percentPopAged75yrsPlusLivi) {
        this.percentPopAged75yrsPlusLivi = percentPopAged75yrsPlusLivi;
    }

    /** (Required) */
    public Object getRankPercentPopAged75yrsPlus() {
        return rankPercentPopAged75yrsPlus;
    }

    /** (Required) */
    public void setRankPercentPopAged75yrsPlus(Object rankPercentPopAged75yrsPlus) {
        this.rankPercentPopAged75yrsPlus = rankPercentPopAged75yrsPlus;
    }

    /** (Required) */
    public Object getPopAged75yrsPlusLivingAlone() {
        return popAged75yrsPlusLivingAlone;
    }

    /** (Required) */
    public void setPopAged75yrsPlusLivingAlone(Object popAged75yrsPlusLivingAlone) {
        this.popAged75yrsPlusLivingAlone = popAged75yrsPlusLivingAlone;
    }

    /** (Required) */
    public Object getRankPopAged75yrsPlusLiving() {
        return rankPopAged75yrsPlusLiving;
    }

    /** (Required) */
    public void setRankPopAged75yrsPlusLiving(Object rankPopAged75yrsPlusLiving) {
        this.rankPopAged75yrsPlusLiving = rankPopAged75yrsPlusLiving;
    }

    /** (Required) */
    public Object getPopAged75yrsPlusLivingAlo1() {
        return popAged75yrsPlusLivingAlo1;
    }

    /** (Required) */
    public void setPopAged75yrsPlusLivingAlo1(Object popAged75yrsPlusLivingAlo1) {
        this.popAged75yrsPlusLivingAlo1 = popAged75yrsPlusLivingAlo1;
    }

    /** (Required) */
    public Object getRankPopAged75yrsPlusLiving1() {
        return rankPopAged75yrsPlusLiving1;
    }

    /** (Required) */
    public void setRankPopAged75yrsPlusLiving1(Object rankPopAged75yrsPlusLiving1) {
        this.rankPopAged75yrsPlusLiving1 = rankPopAged75yrsPlusLiving1;
    }

    /** (Required) */
    public Object getPersonsReceivingDisabilitySe() {
        return personsReceivingDisabilitySe;
    }

    /** (Required) */
    public void setPersonsReceivingDisabilitySe(Object personsReceivingDisabilitySe) {
        this.personsReceivingDisabilitySe = personsReceivingDisabilitySe;
    }

    /** (Required) */
    public Object getRankPersonsReceivingDisabili() {
        return rankPersonsReceivingDisabili;
    }

    /** (Required) */
    public void setRankPersonsReceivingDisabili(Object rankPersonsReceivingDisabili) {
        this.rankPersonsReceivingDisabili = rankPersonsReceivingDisabili;
    }

    /** (Required) */
    public Object getDisabilityPensionPer1000Eli() {
        return disabilityPensionPer1000Eli;
    }

    /** (Required) */
    public void setDisabilityPensionPer1000Eli(Object disabilityPensionPer1000Eli) {
        this.disabilityPensionPer1000Eli = disabilityPensionPer1000Eli;
    }

    /** (Required) */
    public Object getRankDisabilityPensionPer100() {
        return rankDisabilityPensionPer100;
    }

    /** (Required) */
    public void setRankDisabilityPensionPer100(Object rankDisabilityPensionPer100) {
        this.rankDisabilityPensionPer100 = rankDisabilityPensionPer100;
    }

    /** (Required) */
    public Object getAgedCareHighCareBeds() {
        return agedCareHighCareBeds;
    }

    /** (Required) */
    public void setAgedCareHighCareBeds(Object agedCareHighCareBeds) {
        this.agedCareHighCareBeds = agedCareHighCareBeds;
    }

    /** (Required) */
    public Object getAgedCareLowCareBeds() {
        return agedCareLowCareBeds;
    }

    /** (Required) */
    public void setAgedCareLowCareBeds(Object agedCareLowCareBeds) {
        this.agedCareLowCareBeds = agedCareLowCareBeds;
    }

    /** (Required) */
    public Object getAgePensionPer1000EligibleP() {
        return agePensionPer1000EligibleP;
    }

    /** (Required) */
    public void setAgePensionPer1000EligibleP(Object agePensionPer1000EligibleP) {
        this.agePensionPer1000EligibleP = agePensionPer1000EligibleP;
    }

    /** (Required) */
    public Object getRankAgePensionPer1000Eligi() {
        return rankAgePensionPer1000Eligi;
    }

    /** (Required) */
    public void setRankAgePensionPer1000Eligi(Object rankAgePensionPer1000Eligi) {
        this.rankAgePensionPer1000Eligi = rankAgePensionPer1000Eligi;
    }

    /** (Required) */
    public Object getMaleLifeExpectancy() {
        return maleLifeExpectancy;
    }

    /** (Required) */
    public void setMaleLifeExpectancy(Object maleLifeExpectancy) {
        this.maleLifeExpectancy = maleLifeExpectancy;
    }

    /** (Required) */
    public Object getRankMaleLifeExpectancy() {
        return rankMaleLifeExpectancy;
    }

    /** (Required) */
    public void setRankMaleLifeExpectancy(Object rankMaleLifeExpectancy) {
        this.rankMaleLifeExpectancy = rankMaleLifeExpectancy;
    }

    /** (Required) */
    public Object getFemaleLifeExpectancy() {
        return femaleLifeExpectancy;
    }

    /** (Required) */
    public void setFemaleLifeExpectancy(Object femaleLifeExpectancy) {
        this.femaleLifeExpectancy = femaleLifeExpectancy;
    }

    /** (Required) */
    public Object getRankFemaleLifeExpectancy() {
        return rankFemaleLifeExpectancy;
    }

    /** (Required) */
    public void setRankFemaleLifeExpectancy(Object rankFemaleLifeExpectancy) {
        this.rankFemaleLifeExpectancy = rankFemaleLifeExpectancy;
    }

    /** (Required) */
    public Object getPersonsReportingFairOrPoor() {
        return personsReportingFairOrPoor;
    }

    /** (Required) */
    public void setPersonsReportingFairOrPoor(Object personsReportingFairOrPoor) {
        this.personsReportingFairOrPoor = personsReportingFairOrPoor;
    }

    /** (Required) */
    public Object getRankPersonsReportingFairOr() {
        return rankPersonsReportingFairOr;
    }

    /** (Required) */
    public void setRankPersonsReportingFairOr(Object rankPersonsReportingFairOr) {
        this.rankPersonsReportingFairOr = rankPersonsReportingFairOr;
    }

    /** (Required) */
    public Object getFemalesReportingFairOrPoor() {
        return femalesReportingFairOrPoor;
    }

    /** (Required) */
    public void setFemalesReportingFairOrPoor(Object femalesReportingFairOrPoor) {
        this.femalesReportingFairOrPoor = femalesReportingFairOrPoor;
    }

    /** (Required) */
    public Object getRankFemalesReportingFairOr() {
        return rankFemalesReportingFairOr;
    }

    /** (Required) */
    public void setRankFemalesReportingFairOr(Object rankFemalesReportingFairOr) {
        this.rankFemalesReportingFairOr = rankFemalesReportingFairOr;
    }

    /** (Required) */
    public Object getMalesReportingFairOrPoorHe() {
        return malesReportingFairOrPoorHe;
    }

    /** (Required) */
    public void setMalesReportingFairOrPoorHe(Object malesReportingFairOrPoorHe) {
        this.malesReportingFairOrPoorHe = malesReportingFairOrPoorHe;
    }

    /** (Required) */
    public Object getRankMalesReportingFairOrPo() {
        return rankMalesReportingFairOrPo;
    }

    /** (Required) */
    public void setRankMalesReportingFairOrPo(Object rankMalesReportingFairOrPo) {
        this.rankMalesReportingFairOrPo = rankMalesReportingFairOrPo;
    }

    /** (Required) */
    public Object getPercentWhoHaveAHighDegree() {
        return percentWhoHaveAHighDegree;
    }

    /** (Required) */
    public void setPercentWhoHaveAHighDegree(Object percentWhoHaveAHighDegree) {
        this.percentWhoHaveAHighDegree = percentWhoHaveAHighDegree;
    }

    /** (Required) */
    public Object getRankPercentWhoHaveAHighDe() {
        return rankPercentWhoHaveAHighDe;
    }

    /** (Required) */
    public void setRankPercentWhoHaveAHighDe(Object rankPercentWhoHaveAHighDe) {
        this.rankPercentWhoHaveAHighDe = rankPercentWhoHaveAHighDe;
    }

    /** (Required) */
    public Object getPercentOfPersonsSleepingLes() {
        return percentOfPersonsSleepingLes;
    }

    /** (Required) */
    public void setPercentOfPersonsSleepingLes(Object percentOfPersonsSleepingLes) {
        this.percentOfPersonsSleepingLes = percentOfPersonsSleepingLes;
    }

    /** (Required) */
    public Object getRankPercentOfPersonsSleepin() {
        return rankPercentOfPersonsSleepin;
    }

    /** (Required) */
    public void setRankPercentOfPersonsSleepin(Object rankPercentOfPersonsSleepin) {
        this.rankPercentOfPersonsSleepin = rankPercentOfPersonsSleepin;
    }

    /** (Required) */
    public Object getPercentPersonsWithAdequateW() {
        return percentPersonsWithAdequateW;
    }

    /** (Required) */
    public void setPercentPersonsWithAdequateW(Object percentPersonsWithAdequateW) {
        this.percentPersonsWithAdequateW = percentPersonsWithAdequateW;
    }

    /** (Required) */
    public Object getRankPercentPersonsWithAdequ() {
        return rankPercentPersonsWithAdequ;
    }

    /** (Required) */
    public void setRankPercentPersonsWithAdequ(Object rankPercentPersonsWithAdequ) {
        this.rankPercentPersonsWithAdequ = rankPercentPersonsWithAdequ;
    }

    /** (Required) */
    public Object getUnintentionalInjuriesTreated() {
        return unintentionalInjuriesTreated;
    }

    /** (Required) */
    public void setUnintentionalInjuriesTreated(Object unintentionalInjuriesTreated) {
        this.unintentionalInjuriesTreated = unintentionalInjuriesTreated;
    }

    /** (Required) */
    public Object getRankUnintentionalInjuriesTre() {
        return rankUnintentionalInjuriesTre;
    }

    /** (Required) */
    public void setRankUnintentionalInjuriesTre(Object rankUnintentionalInjuriesTre) {
        this.rankUnintentionalInjuriesTre = rankUnintentionalInjuriesTre;
    }

    /** (Required) */
    public Object getIntentionalInjuriesTreatedIn() {
        return intentionalInjuriesTreatedIn;
    }

    /** (Required) */
    public void setIntentionalInjuriesTreatedIn(Object intentionalInjuriesTreatedIn) {
        this.intentionalInjuriesTreatedIn = intentionalInjuriesTreatedIn;
    }

    /** (Required) */
    public Object getRankIntentionalInjuriesTreat() {
        return rankIntentionalInjuriesTreat;
    }

    /** (Required) */
    public void setRankIntentionalInjuriesTreat(Object rankIntentionalInjuriesTreat) {
        this.rankIntentionalInjuriesTreat = rankIntentionalInjuriesTreat;
    }

    /** (Required) */
    public Object getPercentOfUnintentionalHospit() {
        return percentOfUnintentionalHospit;
    }

    /** (Required) */
    public void setPercentOfUnintentionalHospit(Object percentOfUnintentionalHospit) {
        this.percentOfUnintentionalHospit = percentOfUnintentionalHospit;
    }

    /** (Required) */
    public Object getRankPercentOfUnintentionalH() {
        return rankPercentOfUnintentionalH;
    }

    /** (Required) */
    public void setRankPercentOfUnintentionalH(Object rankPercentOfUnintentionalH) {
        this.rankPercentOfUnintentionalH = rankPercentOfUnintentionalH;
    }

    /** (Required) */
    public Object getIndirectStandardisedDeathRat() {
        return indirectStandardisedDeathRat;
    }

    /** (Required) */
    public void setIndirectStandardisedDeathRat(Object indirectStandardisedDeathRat) {
        this.indirectStandardisedDeathRat = indirectStandardisedDeathRat;
    }

    /** (Required) */
    public Object getRankIndirectStandardisedDeat() {
        return rankIndirectStandardisedDeat;
    }

    /** (Required) */
    public void setRankIndirectStandardisedDeat(Object rankIndirectStandardisedDeat) {
        this.rankIndirectStandardisedDeat = rankIndirectStandardisedDeat;
    }

    /** (Required) */
    public Object getAvoidableDeaths0to74YrsFor() {
        return avoidableDeaths0to74YrsFor;
    }

    /** (Required) */
    public void setAvoidableDeaths0to74YrsFor(Object avoidableDeaths0to74YrsFor) {
        this.avoidableDeaths0to74YrsFor = avoidableDeaths0to74YrsFor;
    }

    /** (Required) */
    public Object getRankAvoidableDeaths0to74Yrs() {
        return rankAvoidableDeaths0to74Yrs;
    }

    /** (Required) */
    public void setRankAvoidableDeaths0to74Yrs(Object rankAvoidableDeaths0to74Yrs) {
        this.rankAvoidableDeaths0to74Yrs = rankAvoidableDeaths0to74Yrs;
    }

    /** (Required) */
    public Object getAvoidableDeaths0to74yrsrsFor() {
        return avoidableDeaths0to74yrsrsFor;
    }

    /** (Required) */
    public void setAvoidableDeaths0to74yrsrsFor(Object avoidableDeaths0to74yrsrsFor) {
        this.avoidableDeaths0to74yrsrsFor = avoidableDeaths0to74yrsrsFor;
    }

    /** (Required) */
    public Object getRankAvoidableDeaths0to74yrsr() {
        return rankAvoidableDeaths0to74yrsr;
    }

    /** (Required) */
    public void setRankAvoidableDeaths0to74yrsr(Object rankAvoidableDeaths0to74yrsr) {
        this.rankAvoidableDeaths0to74yrsr = rankAvoidableDeaths0to74yrsr;
    }

    /** (Required) */
    public Object getAvoidableDeaths0to74yrsrsF1() {
        return avoidableDeaths0to74yrsrsF1;
    }

    /** (Required) */
    public void setAvoidableDeaths0to74yrsrsF1(Object avoidableDeaths0to74yrsrsF1) {
        this.avoidableDeaths0to74yrsrsF1 = avoidableDeaths0to74yrsrsF1;
    }

    /** (Required) */
    public Object getRankAvoidableDeaths0to74yr1() {
        return rankAvoidableDeaths0to74yr1;
    }

    /** (Required) */
    public void setRankAvoidableDeaths0to74yr1(Object rankAvoidableDeaths0to74yr1) {
        this.rankAvoidableDeaths0to74yr1 = rankAvoidableDeaths0to74yr1;
    }

    /** (Required) */
    public Object getAvoidableDeaths0to74yrsForR() {
        return avoidableDeaths0to74yrsForR;
    }

    /** (Required) */
    public void setAvoidableDeaths0to74yrsForR(Object avoidableDeaths0to74yrsForR) {
        this.avoidableDeaths0to74yrsForR = avoidableDeaths0to74yrsForR;
    }

    /** (Required) */
    public Object getRankAvoidableDeaths0to74yrs() {
        return rankAvoidableDeaths0to74yrs;
    }

    /** (Required) */
    public void setRankAvoidableDeaths0to74yrs(Object rankAvoidableDeaths0to74yrs) {
        this.rankAvoidableDeaths0to74yrs = rankAvoidableDeaths0to74yrs;
    }

    /** (Required) */
    public Object getPrimaryHealthNetwork() {
        return primaryHealthNetwork;
    }

    /** (Required) */
    public void setPrimaryHealthNetwork(Object primaryHealthNetwork) {
        this.primaryHealthNetwork = primaryHealthNetwork;
    }

    /** (Required) */
    public Object getPrimaryCarePartnership() {
        return primaryCarePartnership;
    }

    /** (Required) */
    public void setPrimaryCarePartnership(Object primaryCarePartnership) {
        this.primaryCarePartnership = primaryCarePartnership;
    }

    /** (Required) */
    public Object getNumberOfHospitalsAndHealth() {
        return numberOfHospitalsAndHealth;
    }

    /** (Required) */
    public void setNumberOfHospitalsAndHealth(Object numberOfHospitalsAndHealth) {
        this.numberOfHospitalsAndHealth = numberOfHospitalsAndHealth;
    }

    /** (Required) */
    public Object getNumberOfPublicHospitalsAnd() {
        return numberOfPublicHospitalsAnd;
    }

    /** (Required) */
    public void setNumberOfPublicHospitalsAnd(Object numberOfPublicHospitalsAnd) {
        this.numberOfPublicHospitalsAnd = numberOfPublicHospitalsAnd;
    }

    /** (Required) */
    public Object getNumberOfPrivateHospitalsAnd() {
        return numberOfPrivateHospitalsAnd;
    }

    /** (Required) */
    public void setNumberOfPrivateHospitalsAnd(Object numberOfPrivateHospitalsAnd) {
        this.numberOfPrivateHospitalsAnd = numberOfPrivateHospitalsAnd;
    }

    public Object getGPsPer1000Pop() {
        return gPsPer1000Pop;
    }

    public void setGPsPer1000Pop(Object gPsPer1000Pop) {
        this.gPsPer1000Pop = gPsPer1000Pop;
    }

    /** (Required) */
    public Object getRankGPsPer1000Pop() {
        return rankGPsPer1000Pop;
    }

    /** (Required) */
    public void setRankGPsPer1000Pop(Object rankGPsPer1000Pop) {
        this.rankGPsPer1000Pop = rankGPsPer1000Pop;
    }

    public Object getGPSitesPer1000Pop() {
        return gPSitesPer1000Pop;
    }

    public void setGPSitesPer1000Pop(Object gPSitesPer1000Pop) {
        this.gPSitesPer1000Pop = gPSitesPer1000Pop;
    }

    /** (Required) */
    public Object getRankGPSitesPer1000Pop() {
        return rankGPSitesPer1000Pop;
    }

    /** (Required) */
    public void setRankGPSitesPer1000Pop(Object rankGPSitesPer1000Pop) {
        this.rankGPSitesPer1000Pop = rankGPSitesPer1000Pop;
    }

    /** (Required) */
    public Object getAlliedHealthSitesPer1000Po() {
        return alliedHealthSitesPer1000Po;
    }

    /** (Required) */
    public void setAlliedHealthSitesPer1000Po(Object alliedHealthSitesPer1000Po) {
        this.alliedHealthSitesPer1000Po = alliedHealthSitesPer1000Po;
    }

    /** (Required) */
    public Object getRankAlliedHealthSitesPer10() {
        return rankAlliedHealthSitesPer10;
    }

    /** (Required) */
    public void setRankAlliedHealthSitesPer10(Object rankAlliedHealthSitesPer10) {
        this.rankAlliedHealthSitesPer10 = rankAlliedHealthSitesPer10;
    }

    /** (Required) */
    public Object getDentalServicesPer1000Pop() {
        return dentalServicesPer1000Pop;
    }

    /** (Required) */
    public void setDentalServicesPer1000Pop(Object dentalServicesPer1000Pop) {
        this.dentalServicesPer1000Pop = dentalServicesPer1000Pop;
    }

    /** (Required) */
    public Object getRankDentalServicesPer1000P() {
        return rankDentalServicesPer1000P;
    }

    /** (Required) */
    public void setRankDentalServicesPer1000P(Object rankDentalServicesPer1000P) {
        this.rankDentalServicesPer1000P = rankDentalServicesPer1000P;
    }

    /** (Required) */
    public Object getPharmaciesPer1000Pop() {
        return pharmaciesPer1000Pop;
    }

    /** (Required) */
    public void setPharmaciesPer1000Pop(Object pharmaciesPer1000Pop) {
        this.pharmaciesPer1000Pop = pharmaciesPer1000Pop;
    }

    /** (Required) */
    public Object getRankPharmaciesPer1000Pop() {
        return rankPharmaciesPer1000Pop;
    }

    /** (Required) */
    public void setRankPharmaciesPer1000Pop(Object rankPharmaciesPer1000Pop) {
        this.rankPharmaciesPer1000Pop = rankPharmaciesPer1000Pop;
    }

    /** (Required) */
    public Object getPercentNearPublicTransport() {
        return percentNearPublicTransport;
    }

    /** (Required) */
    public void setPercentNearPublicTransport(Object percentNearPublicTransport) {
        this.percentNearPublicTransport = percentNearPublicTransport;
    }

    /** (Required) */
    public Object getRankPercentNearPublicTransp() {
        return rankPercentNearPublicTransp;
    }

    /** (Required) */
    public void setRankPercentNearPublicTransp(Object rankPercentNearPublicTransp) {
        this.rankPercentNearPublicTransp = rankPercentNearPublicTransp;
    }

    /** (Required) */
    public Object getPercentWithPrivateHealthIns() {
        return percentWithPrivateHealthIns;
    }

    /** (Required) */
    public void setPercentWithPrivateHealthIns(Object percentWithPrivateHealthIns) {
        this.percentWithPrivateHealthIns = percentWithPrivateHealthIns;
    }

    /** (Required) */
    public Object getRankPercentWithPrivateHealt() {
        return rankPercentWithPrivateHealt;
    }

    /** (Required) */
    public void setRankPercentWithPrivateHealt(Object rankPercentWithPrivateHealt) {
        this.rankPercentWithPrivateHealt = rankPercentWithPrivateHealt;
    }

    /** (Required) */
    public Object getHospitalInpatientSeparations() {
        return hospitalInpatientSeparations;
    }

    /** (Required) */
    public void setHospitalInpatientSeparations(Object hospitalInpatientSeparations) {
        this.hospitalInpatientSeparations = hospitalInpatientSeparations;
    }

    /** (Required) */
    public Object getRankHospitalInpatientSeparat() {
        return rankHospitalInpatientSeparat;
    }

    /** (Required) */
    public void setRankHospitalInpatientSeparat(Object rankHospitalInpatientSeparat) {
        this.rankHospitalInpatientSeparat = rankHospitalInpatientSeparat;
    }

    /** (Required) */
    public Object getPercentInpatientSeparationsF() {
        return percentInpatientSeparationsF;
    }

    /** (Required) */
    public void setPercentInpatientSeparationsF(Object percentInpatientSeparationsF) {
        this.percentInpatientSeparationsF = percentInpatientSeparationsF;
    }

    /** (Required) */
    public Object getRankPercentInpatientSeparati() {
        return rankPercentInpatientSeparati;
    }

    /** (Required) */
    public void setRankPercentInpatientSeparati(Object rankPercentInpatientSeparati) {
        this.rankPercentInpatientSeparati = rankPercentInpatientSeparati;
    }

    /** (Required) */
    public Object getMainPublicHospitalAttendedF() {
        return mainPublicHospitalAttendedF;
    }

    /** (Required) */
    public void setMainPublicHospitalAttendedF(Object mainPublicHospitalAttendedF) {
        this.mainPublicHospitalAttendedF = mainPublicHospitalAttendedF;
    }

    /** (Required) */
    public Object getMainPublicHospitalAttendedP() {
        return mainPublicHospitalAttendedP;
    }

    /** (Required) */
    public void setMainPublicHospitalAttendedP(Object mainPublicHospitalAttendedP) {
        this.mainPublicHospitalAttendedP = mainPublicHospitalAttendedP;
    }

    /** (Required) */
    public Object getRankMainPublicHospitalAtten() {
        return rankMainPublicHospitalAtten;
    }

    /** (Required) */
    public void setRankMainPublicHospitalAtten(Object rankMainPublicHospitalAtten) {
        this.rankMainPublicHospitalAtten = rankMainPublicHospitalAtten;
    }

    /** (Required) */
    public Object getAverageLengthOfStayInDays() {
        return averageLengthOfStayInDays;
    }

    /** (Required) */
    public void setAverageLengthOfStayInDays(Object averageLengthOfStayInDays) {
        this.averageLengthOfStayInDays = averageLengthOfStayInDays;
    }

    /** (Required) */
    public Object getRankAverageLengthOfStayIn() {
        return rankAverageLengthOfStayIn;
    }

    /** (Required) */
    public void setRankAverageLengthOfStayIn(Object rankAverageLengthOfStayIn) {
        this.rankAverageLengthOfStayIn = rankAverageLengthOfStayIn;
    }

    /** (Required) */
    public Object getAverageLengthOfStayForAll() {
        return averageLengthOfStayForAll;
    }

    /** (Required) */
    public void setAverageLengthOfStayForAll(Object averageLengthOfStayForAll) {
        this.averageLengthOfStayForAll = averageLengthOfStayForAll;
    }

    /** (Required) */
    public Object getRankAverageLengthOfStayFor() {
        return rankAverageLengthOfStayFor;
    }

    /** (Required) */
    public void setRankAverageLengthOfStayFor(Object rankAverageLengthOfStayFor) {
        this.rankAverageLengthOfStayFor = rankAverageLengthOfStayFor;
    }

    /** (Required) */
    public Object getPerAnnumPercentChangeInSep() {
        return perAnnumPercentChangeInSep;
    }

    /** (Required) */
    public void setPerAnnumPercentChangeInSep(Object perAnnumPercentChangeInSep) {
        this.perAnnumPercentChangeInSep = perAnnumPercentChangeInSep;
    }

    /** (Required) */
    public Object getRankPerAnnumPercentChangeI() {
        return rankPerAnnumPercentChangeI;
    }

    /** (Required) */
    public void setRankPerAnnumPercentChangeI(Object rankPerAnnumPercentChangeI) {
        this.rankPerAnnumPercentChangeI = rankPerAnnumPercentChangeI;
    }

    /** (Required) */
    public Object getPerAnnumPercentProjectedCha() {
        return perAnnumPercentProjectedCha;
    }

    /** (Required) */
    public void setPerAnnumPercentProjectedCha(Object perAnnumPercentProjectedCha) {
        this.perAnnumPercentProjectedCha = perAnnumPercentProjectedCha;
    }

    /** (Required) */
    public Object getRankPerAnnumPercentProjecte() {
        return rankPerAnnumPercentProjecte;
    }

    /** (Required) */
    public void setRankPerAnnumPercentProjecte(Object rankPerAnnumPercentProjecte) {
        this.rankPerAnnumPercentProjecte = rankPerAnnumPercentProjecte;
    }

    public Object getACSCsPer1000PopTotal() {
        return aCSCsPer1000PopTotal;
    }

    public void setACSCsPer1000PopTotal(Object aCSCsPer1000PopTotal) {
        this.aCSCsPer1000PopTotal = aCSCsPer1000PopTotal;
    }

    /** (Required) */
    public Object getRankACSCsPer1000PopTotal() {
        return rankACSCsPer1000PopTotal;
    }

    /** (Required) */
    public void setRankACSCsPer1000PopTotal(Object rankACSCsPer1000PopTotal) {
        this.rankACSCsPer1000PopTotal = rankACSCsPer1000PopTotal;
    }

    public Object getACSCsPer1000PopAcute() {
        return aCSCsPer1000PopAcute;
    }

    public void setACSCsPer1000PopAcute(Object aCSCsPer1000PopAcute) {
        this.aCSCsPer1000PopAcute = aCSCsPer1000PopAcute;
    }

    /** (Required) */
    public Object getRankACSCsPer1000PopAcute() {
        return rankACSCsPer1000PopAcute;
    }

    /** (Required) */
    public void setRankACSCsPer1000PopAcute(Object rankACSCsPer1000PopAcute) {
        this.rankACSCsPer1000PopAcute = rankACSCsPer1000PopAcute;
    }

    public Object getACSCsPer1000PopChronic() {
        return aCSCsPer1000PopChronic;
    }

    public void setACSCsPer1000PopChronic(Object aCSCsPer1000PopChronic) {
        this.aCSCsPer1000PopChronic = aCSCsPer1000PopChronic;
    }

    /** (Required) */
    public Object getRankACSCsPer1000PopChronic() {
        return rankACSCsPer1000PopChronic;
    }

    /** (Required) */
    public void setRankACSCsPer1000PopChronic(Object rankACSCsPer1000PopChronic) {
        this.rankACSCsPer1000PopChronic = rankACSCsPer1000PopChronic;
    }

    public Object getACSCsPer1000PopVaccinePrev() {
        return aCSCsPer1000PopVaccinePrev;
    }

    public void setACSCsPer1000PopVaccinePrev(Object aCSCsPer1000PopVaccinePrev) {
        this.aCSCsPer1000PopVaccinePrev = aCSCsPer1000PopVaccinePrev;
    }

    /** (Required) */
    public Object getRankACSCsPer1000PopVaccine() {
        return rankACSCsPer1000PopVaccine;
    }

    /** (Required) */
    public void setRankACSCsPer1000PopVaccine(Object rankACSCsPer1000PopVaccine) {
        this.rankACSCsPer1000PopVaccine = rankACSCsPer1000PopVaccine;
    }

    /** (Required) */
    public Object getEmergencyDepartmentPresentati() {
        return emergencyDepartmentPresentati;
    }

    /** (Required) */
    public void setEmergencyDepartmentPresentati(Object emergencyDepartmentPresentati) {
        this.emergencyDepartmentPresentati = emergencyDepartmentPresentati;
    }

    /** (Required) */
    public Object getRankEmergencyDepartmentPrese() {
        return rankEmergencyDepartmentPrese;
    }

    /** (Required) */
    public void setRankEmergencyDepartmentPrese(Object rankEmergencyDepartmentPrese) {
        this.rankEmergencyDepartmentPrese = rankEmergencyDepartmentPrese;
    }

    /** (Required) */
    public Object getPrimaryCareTypePresentations() {
        return primaryCareTypePresentations;
    }

    /** (Required) */
    public void setPrimaryCareTypePresentations(Object primaryCareTypePresentations) {
        this.primaryCareTypePresentations = primaryCareTypePresentations;
    }

    /** (Required) */
    public Object getRankPrimaryCareTypePresenta() {
        return rankPrimaryCareTypePresenta;
    }

    /** (Required) */
    public void setRankPrimaryCareTypePresenta(Object rankPrimaryCareTypePresenta) {
        this.rankPrimaryCareTypePresenta = rankPrimaryCareTypePresenta;
    }

    /** (Required) */
    public Object getChildProtectionInvestigations() {
        return childProtectionInvestigations;
    }

    /** (Required) */
    public void setChildProtectionInvestigations(Object childProtectionInvestigations) {
        this.childProtectionInvestigations = childProtectionInvestigations;
    }

    /** (Required) */
    public Object getRankChildProtectionInvestiga() {
        return rankChildProtectionInvestiga;
    }

    /** (Required) */
    public void setRankChildProtectionInvestiga(Object rankChildProtectionInvestiga) {
        this.rankChildProtectionInvestiga = rankChildProtectionInvestiga;
    }

    /** (Required) */
    public Object getChildProtectionSubstantiation() {
        return childProtectionSubstantiation;
    }

    /** (Required) */
    public void setChildProtectionSubstantiation(Object childProtectionSubstantiation) {
        this.childProtectionSubstantiation = childProtectionSubstantiation;
    }

    /** (Required) */
    public Object getRankChildProtectionSubstanti() {
        return rankChildProtectionSubstanti;
    }

    /** (Required) */
    public void setRankChildProtectionSubstanti(Object rankChildProtectionSubstanti) {
        this.rankChildProtectionSubstanti = rankChildProtectionSubstanti;
    }

    /** (Required) */
    public Object getNumberOfChildFIRSTAssessmen() {
        return numberOfChildFIRSTAssessmen;
    }

    /** (Required) */
    public void setNumberOfChildFIRSTAssessmen(Object numberOfChildFIRSTAssessmen) {
        this.numberOfChildFIRSTAssessmen = numberOfChildFIRSTAssessmen;
    }

    /** (Required) */
    public Object getRankNumberOfChildFIRSTAsse() {
        return rankNumberOfChildFIRSTAsse;
    }

    /** (Required) */
    public void setRankNumberOfChildFIRSTAsse(Object rankNumberOfChildFIRSTAsse) {
        this.rankNumberOfChildFIRSTAsse = rankNumberOfChildFIRSTAsse;
    }

    public Object getGPAttendancesPer1000PopMal() {
        return gPAttendancesPer1000PopMal;
    }

    public void setGPAttendancesPer1000PopMal(Object gPAttendancesPer1000PopMal) {
        this.gPAttendancesPer1000PopMal = gPAttendancesPer1000PopMal;
    }

    /** (Required) */
    public Object getRankGPAttendancesPer1000Po() {
        return rankGPAttendancesPer1000Po;
    }

    /** (Required) */
    public void setRankGPAttendancesPer1000Po(Object rankGPAttendancesPer1000Po) {
        this.rankGPAttendancesPer1000Po = rankGPAttendancesPer1000Po;
    }

    public Object getGPAttendancesPer1000PopFem() {
        return gPAttendancesPer1000PopFem;
    }

    public void setGPAttendancesPer1000PopFem(Object gPAttendancesPer1000PopFem) {
        this.gPAttendancesPer1000PopFem = gPAttendancesPer1000PopFem;
    }

    /** (Required) */
    public Object getRankGPAttendancesPer10001() {
        return rankGPAttendancesPer10001;
    }

    /** (Required) */
    public void setRankGPAttendancesPer10001(Object rankGPAttendancesPer10001) {
        this.rankGPAttendancesPer10001 = rankGPAttendancesPer10001;
    }

    public Object getGPAttendancesPer1000PopTot() {
        return gPAttendancesPer1000PopTot;
    }

    public void setGPAttendancesPer1000PopTot(Object gPAttendancesPer1000PopTot) {
        this.gPAttendancesPer1000PopTot = gPAttendancesPer1000PopTot;
    }

    /** (Required) */
    public Object getRankGPAttendancesPer10002() {
        return rankGPAttendancesPer10002;
    }

    /** (Required) */
    public void setRankGPAttendancesPer10002(Object rankGPAttendancesPer10002) {
        this.rankGPAttendancesPer10002 = rankGPAttendancesPer10002;
    }

    public Object getHACCClientsAged0to64yrsPer() {
        return hACCClientsAged0to64yrsPer;
    }

    public void setHACCClientsAged0to64yrsPer(Object hACCClientsAged0to64yrsPer) {
        this.hACCClientsAged0to64yrsPer = hACCClientsAged0to64yrsPer;
    }

    /** (Required) */
    public Object getRankHACCClientsAged0to64yrs() {
        return rankHACCClientsAged0to64yrs;
    }

    /** (Required) */
    public void setRankHACCClientsAged0to64yrs(Object rankHACCClientsAged0to64yrs) {
        this.rankHACCClientsAged0to64yrs = rankHACCClientsAged0to64yrs;
    }

    public Object getHACCClientsAged65yrsPlusPer() {
        return hACCClientsAged65yrsPlusPer;
    }

    public void setHACCClientsAged65yrsPlusPer(Object hACCClientsAged65yrsPlusPer) {
        this.hACCClientsAged65yrsPlusPer = hACCClientsAged65yrsPlusPer;
    }

    /** (Required) */
    public Object getRankHACCClientsAged65yrsPlu() {
        return rankHACCClientsAged65yrsPlu;
    }

    /** (Required) */
    public void setRankHACCClientsAged65yrsPlu(Object rankHACCClientsAged65yrsPlu) {
        this.rankHACCClientsAged65yrsPlu = rankHACCClientsAged65yrsPlu;
    }

    /** (Required) */
    public Object getNoClientsWhoReceivedAlcohol() {
        return noClientsWhoReceivedAlcohol;
    }

    /** (Required) */
    public void setNoClientsWhoReceivedAlcohol(Object noClientsWhoReceivedAlcohol) {
        this.noClientsWhoReceivedAlcohol = noClientsWhoReceivedAlcohol;
    }

    /** (Required) */
    public Object getRankNoClientsWhoReceivedAl() {
        return rankNoClientsWhoReceivedAl;
    }

    /** (Required) */
    public void setRankNoClientsWhoReceivedAl(Object rankNoClientsWhoReceivedAl) {
        this.rankNoClientsWhoReceivedAl = rankNoClientsWhoReceivedAl;
    }

    /** (Required) */
    public Object getRegisteredMentalMealthClient() {
        return registeredMentalMealthClient;
    }

    /** (Required) */
    public void setRegisteredMentalMealthClient(Object registeredMentalMealthClient) {
        this.registeredMentalMealthClient = registeredMentalMealthClient;
    }

    /** (Required) */
    public Object getRankRegisteredMentalMealthC() {
        return rankRegisteredMentalMealthC;
    }

    /** (Required) */
    public void setRankRegisteredMentalMealthC(Object rankRegisteredMentalMealthC) {
        this.rankRegisteredMentalMealthC = rankRegisteredMentalMealthC;
    }
}
