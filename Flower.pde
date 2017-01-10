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

import java.util.Calendar;

float x, y; // Positions of mouse when clicked with mouse easing
float posX, posY; // Positions of mouse when clicked
int day, hour; // Holds int values of days and hours
int subBranches; // Number of sub branches to draw

Calendar cal = Calendar.getInstance(); // Calender object

// Color Ranges (lightest to darkest)
color[] purpleColors = {#8D79AE, #684F91, #482E74, #2E1557, #19053A};
color[] magentaColors = {#AD74A8, #90488A, #74276D, #570E51, #3A0035};
color[] pinkColors = {#D38DAF, #B05883, #8D2F5D, #6A123D, #460023};
color[] samonPinkColors = {#E99BAF, #C26179, #9B344E, #74132C, #4E0013};
color[] redColors = {#FFAAAA, #D46A6A, #AA3939, #801515, #550000};
color[] amberColors = {#FFC4AA, #D48B6A, #AA5C39, #803615, #551A00};
color[] orangeColors = {#551A00, #D49D6A, #AA6F39, #804815, #552900};
color[] darkYellowColors = {#AA9539, #FFD41A, #DCBB30, #786C36, #474128}; 
color[] yellowColors = {#A6A938, #F8FE19, #D6DA2F, #767836, #464728}; 
color[] lightGreenColors = {#84A136, #B9F418, #A4D02D, #617233, #3B4326}; 
color[] greenColors = {#599532, #68E517, #65C12A, #476A30, #2E3E23}; 
color[] darkGreenColors = {#2D8632, #15D221, #25AD2E, #2B5F2E, #203821}; 

// All color ranges
color[][] colorRanges = { darkGreenColors, greenColors, lightGreenColors,
                          yellowColors, darkYellowColors, orangeColors,
                          amberColors, redColors, samonPinkColors,
                          pinkColors, magentaColors, purpleColors};
                      

void setup() { 
    size(620, 620);
    background(255);  //set background white
    colorMode(RGB);   //set colors to Hue, Saturation, Brightness mode
    frameRate(60);
    surface.setTitle("Flower");
   
    noStroke();
    smooth();
}

void draw() {  // draw loop  
    
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
    x = lerp(x, posX, 0.05);
    y = lerp(y, posY, 0.05);
    
    // Draw flower
    drawFlowerBranch(width / 2, height / 2 + 325, 2 + x / 100, 25 + (day * 7), PI / 2, 5, subBranches, false); // Draw left side
    drawFlowerBranch(width / 2, height / 2 + 325, 2 + x / 100, 25 + (day * 7), PI / 2, 5, subBranches, true); // Draw right side
    
}

// Recursive branch drawing function
void drawFlowerBranch(float startX, float startY, float curlCoefficient, float branchLen, float baseAngle, int mainBranchRecursions, int subBranchRecursions, boolean rightSide) {
  
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

void mouseDragged() {
  // Only update mouse positions when mouse is dragged
  posX = mouseX; 
  posY = mouseY;
}