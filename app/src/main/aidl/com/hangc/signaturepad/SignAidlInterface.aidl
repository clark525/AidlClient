// SignAidlInterface.aidl
package com.hangc.signaturepad;

// Declare any non-default types here with import statements

interface SignAidlInterface {

   int satrtSign(int timeOut, int startX, int endX, int startY, int endY, String pngPath, String txtPath , in byte[] message);

   int cancelSign(in byte[] message);

   int clearSign(in byte[] message);

   int confirmSign(in byte[] message);

   int getRandom();

}