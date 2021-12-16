package uj.java.gvt;

import java.util.ArrayList;

public record GvtVersion(long number, String message, ArrayList<String> addedFiles, ArrayList<String> committedFiles){}
