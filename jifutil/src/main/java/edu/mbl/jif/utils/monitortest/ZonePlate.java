/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.utils.monitortest;

//import edu.mbl.jif.camera.display.DisplayImageFrame;
//import edu.mbl.jif.gui.imaging.FrameImageDisplay_1;
//import edu.mbl.jif.gui.imaging.ImageArrayDisplayPanel;
//import edu.mbl.jif.imaging.ImageFactoryGrayScale;
import java.io.File;

/**
 *
 * @author GBH
 */
public class ZonePlate {


    /* Copyright (C) 1978-1995 Ken Turkowski. <turk@computer.org>
     *
     * All rights reserved.
     *
     * Warranty Information
     *  Even though I have reviewed this software, I make no warranty
     *  or representation, either express or implied, with respect to this
     *  software, its quality, accuracy, merchantability, or fitness for a
     *  particular purpose.  As a result, this software is provided "as is,"
     *  and you, its user, are assuming the entire risk as to its quality
     *  and accuracy.
     *
     * This code may be used and freely distributed as long as it includes
     * this copyright notice and the above warranty information.
     */
    static byte[] sineTab = new byte[256];

    /*******************************************************************************
     * ZonePlate
     *******************************************************************************/
    static byte[] ZonePlate(int width, int height, int scale) {
        int cX, cY;
        int i, j;
        int x, y;
        int d;
        byte[] v = new byte[width * height];
        cX = width / 2;
        cY = height / 2;
        y = -cY;
        for (i = height; i > 0; i--) {
            x = -cX;
            for (j = width; j > 0; j--) {
                d = ((x * x + y * y) * scale) >> 8;
                v[i * width - j] = sineTab[d & 0xFF];
                //fd.
                x++;
            }
            y++;
        }
        return v;
    }

    /*******************************************************************************
     * MakeSineTab
     *******************************************************************************/
    static void makeSineTab() {
        int i;
        for (i = 0; i < 256; i++) {
            sineTab[i] = (byte) ((127.5 * Math.sin(Math.PI * (float) i - 127.5) / 127.5) + 127.5);
        }
    }

    /*******************************************************************************
     * main
     *******************************************************************************/
    public static void main(String[] args) {


        int width = 256;
        int height = 256;
        int scale = 100;
        String outFile;
        File fd;

//	for (argc--, argv++; argc-- > 0; argv++) {
//		if (argv[0][0] == '-') {
//			switch (argv[0][1]) {
//				case 'w':
//					argc--; argv++;
//					width = atoi(argv[0]);
//					break;
//				case 'h':
//					argc--; argv++;
//					height = atoi(argv[0]);
//					break;
//				case 's':
//					argc--; argv++;
//					scale = atoi(argv[0]);
//					break;
//				case 'o':
//					argc--; argv++;
//					outFile = argv[0];
//					break;
//			}
//		}
//		else {
//		}
        //}

//	if (width == 0 || height == 0 || scale == 0 || outFile == NULL) {
//		printf("Usage: ZonePlate -w <width> -h <height> -s <scale> -o <outFile>\n");
//		exit(1);
//	}

//	if ((fd = fopen(outFile, "wb")) == NULL) {
//		printf("Can't open \"%s\"\n", outFile);
//		exit(2);
//	}
        makeSineTab();

        byte[] v = ZonePlate(width, height, scale);
//        FrameImageDisplay_1 f = new FrameImageDisplay_1(ImageFactoryGrayScale.createImage(width, height, 8, v), "Zone");
//        f.setVisible(true);
    }

}