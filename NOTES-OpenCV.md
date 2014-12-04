# Notes on OpenCV

Note that OpenCV allocates colored RGB (OpenCF reads as as BGR!! Not RGB) images to a three channel (and a fourth for the transparency, that is, alpha channel) [array](http://docs.opencv.org/java/org/opencv/core/Mat.html), following the BGR order with the higher values corresponding to brighter pixels.

## General Notes

- OpenCV reads images as BGR not RGB.
- When using Imgpro.cvtColor, transform images to 32-bit first or infomation will be lost
