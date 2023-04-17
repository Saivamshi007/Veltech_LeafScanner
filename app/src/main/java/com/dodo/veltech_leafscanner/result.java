package com.dodo.veltech_leafscanner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class result {

    @SerializedName("bestMatch")
    private String bestMatch;

    @SerializedName("results")
    public List<Species> results;


    public result(String bestMatch,List<Species> results) {

        this.bestMatch = bestMatch;
        this.results = results;
    }

    public String getBestMatch() {
        return bestMatch;
    }

    public void setBestMatch(String bestMatch) {
        this.bestMatch = bestMatch;
    }

    public List<Species> getResults() {
        return results;
    }

    public void setResults(List<Species> results) {
        this.results = results;
    }
    public class Species {

        @SerializedName("scientificNameWithoutAuthor")
        @Expose
        public String scientificNameWithoutAuthor;

        @SerializedName("commonNames")
        @Expose
        public ArrayList<String> commonNames;


        public Species(String scientificNameWithoutAuthor, ArrayList<String> commonNames) {
            this.scientificNameWithoutAuthor = scientificNameWithoutAuthor;

            this.commonNames = commonNames;
        }

        public String getScientificNameWithoutAuthor() {
            return scientificNameWithoutAuthor;
        }

        public void setScientificNameWithoutAuthor(String scientificNameWithoutAuthor) {
            this.scientificNameWithoutAuthor = scientificNameWithoutAuthor;
        }


        public ArrayList<String> getCommonNames() {
            return commonNames;
        }

        public void setCommonNames(ArrayList<String> commonNames) {
            this.commonNames = commonNames;
        }

        public Species() {
        }
    }

}
