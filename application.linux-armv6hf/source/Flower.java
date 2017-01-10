import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Calendar; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Flower extends PApplet {

////////////////////////////////////////////////////////////////////////
///                        Aimee's Flower                            ///
////////////////////////////////////////////////////////////////////////
///                                                                  ///
/// Written by Matthew Carney =^-^= (20.4.2016)                      ///
///                                                                  ///
///       Inspired by this anonymous piece of genius                 ///
///       http://www.openprocessing.org/sketch/16970                 ///
///                                                                  ///
////////////////////////////////////////////////////////////////////////



float x, y; // Positions of mouse when clicked with mouse easing
float posX, posY; // Positions of mouse when clicked
int day, hour; // Holds int values of days and hours
int subBranches; // Number of sub branches to draw

Calendar cal = Calendar.getInstance(); // Calender object

// Color Ranges (lightest to darkest)
int[] purpleColors = {0xff8D79AE, 0xff684F91, 0xff482E74, 0xff2E1557, 0xff19053A};
int[] magentaColors = {0xffAD74A8, 0xff90488A, 0xff74276D, 0xff570E51, 0xff3A0035};
int[] pinkColors = {0xffD38DAF, 0xffB05883, 0xff8D2F5D, 0xff6A123D, 0xff460023};
int[] samonPinkColors = {0xffE99BAF, 0xffC26179, 0xff9B344E, 0xff74132C, 0xff4E0013};
int[] redColors = {0xffFFAAAA, 0xffD46A6A, 0xffAA3939, 0xff801515, 0xff550000};
int[] amberColors = {0xffFFC4AA, 0xffD48B6A, 0xffAA5C39, 0xff803615, 0xff551A00};
int[] orangeColors = {0xff551A00, 0xffD49D6A, 0xffAA6F39, 0xff804815, 0xff552900};
int[] darkYellowColors = {0xffAA9539, 0xffFFD41A, 0xffDCBB30, 0xff786C36, 0xff474128}; 
int[] yellowColors = {0xffA6A938, 0xffF8FE19, 0xffD6DA2F, 0xff767836, 0xff464728}; 
int[] lightGreenColors = {0xff84A136, 0xffB9F418, 0xffA4D02D, 0xff617233, 0xff3B4326}; 
int[] greenColors = {0xff599532, 0xff68E517, 0xff65C12A, 0xff476A30, 0xff2E3E23}; 
int[] darkGreenColors = {0xff2D8632, 0xff15D221, 0xff25AD2E, 0xff2B5F2E, 0xff203821}; 

// All color ranges
int[][] colorRanges = { darkGreenColors, greenColors, lightGreenColors,
                          yellowColors, darkYellowColors, orangeColors,
                          amberColors, redColors, samonPinkColors,
                          pinkColors, magentaColors, purpleColors};
                      

public void setup() { 
    
    background(255);  //set background white
    colorMode(RGB);   //set colors to Hue, Saturation, Brightness mode
    frameRate(60);
    surface.setTitle("Flower");
   
    noStroke();
    
}

public void draw() {  // draw loop  
    
    fill(255, 10); // White, slight modification to alpha (transparency)
    rect(0, 0, width, height);
    
    day = cal.get(Calendar.DAY_OF_WEEK); // Get day of the week (0 for sunday, 6 for saturday)
    hour = cal.get(Calendar.HOUR_OF_DAY); // Get hour of day (0 to 23)
    
    if (hour > 11) {
      hour -= 12; // Keep hours within color array bounds
    }
    
    // determine number of sub branches (curls) by day of the week (full bloom at end of the week)
    if (day < 2) {
      subBranches = 5;
    } else if (day < 4) {
      subBranches = 6;
    } else if (day < 6) {
      subBranches = 7;
    } else if (day == 6) {
      subBranches = 8; 
    }
    
    // Smooth mouse movement with linear interpolation
    x = lerp(x, posX, 0.05f);
    y = lerp(y, posY, 0.05f);
    
    // Draw flower
    drawFlowerBranch(width / 2, height / 2 + 325, 2 + x / 100, 25 + (day * 7), PI / 2, 5, subBranches, false); // Draw left side
    drawFlowerBranch(width / 2, height / 2 + 325, 2 + x / 100, 25 + (day * 7), PI / 2, 5, subBranches, true); // Draw right side
    
}

// Recursive branch drawing function
public void drawFlowerBranch(float startX, float startY, float curlCoefficient, float branchLen, float baseAngle, int mainBranchRecursions, int subBranchRecursions, boolean rightSide) {
  
  // Colour branch segment
  if (mainBranchRecursions % 2 == 0) { // if recursion is even
    stroke(0);
  } else {
    stroke(colorRanges[hour][mainBranchRecursions - 1]); // Color range determined by hour of day, color used determined by main branch recursions
  }
  
  // If we are still drawing main branches
  if (mainBranchRecursions > 0) {
    float nextX, nextY, nextAngle; // Stores position and angle of next branch segment
    
    // Calculate branch position diffently depending on which side of flower we are drawing
    if (rightSide) {
      // Get next branch starting point by using baseAngle to calculate new position in regards to general flower curve
      nextX = startX - branchLen * cos(baseAngle);
      nextY = startY - branchLen * sin(baseAngle);
    } else {
      nextX = startX + branchLen * cos(baseAngle);
      nextY = startY - branchLen * sin(baseAngle);
    }
    
    line(startX, startY, nextX, nextY); // Draw line from start point to start point of next branch segment
    
    nextAngle = baseAngle + PI / curlCoefficient; // Work out next branch segment (again working out angle in regards to overall flower curvature)
    
    // If we are still drawing sub branches
    if (subBranchRecursions > 0) {
      // Draw a sub branch
      drawFlowerBranch(nextX, nextY, curlCoefficient + 5, branchLen, y * baseAngle / 300, mainBranchRecursions, subBranchRecursions - 1, rightSide);
    }
    
    // Draw a main branch
    drawFlowerBranch(nextX, nextY, curlCoefficient / 2, branchLen / 2, nextAngle, mainBranchRecursions - 1, subBranchRecursions, rightSide);
    
    // NOTE:
    // Sub branches and main branches are drawn in slightly different ways, sub branches half coeff and branch len each recursion
    // so the recursion is not infinite with no variation. Main branches also use the next predicted angle in curve, sub branch angle is
    // calculated differently. This is because unlike main branches, sub branches are recursed into each time a new main branch is made, ie
    // they arent as big (angle and length wise)
  }
}

public void mouseDragged() {
  // Only update mouse positions when mouse is dragged
  posX = mouseX; 
  posY = mouseY;
}
  public void settings() {  size(620, 620);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Flower" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
