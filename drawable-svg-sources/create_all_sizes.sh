#!/bin/bash
for i in *svg; do
  length=${#i}
  newLength=length-4
  newName=${i:0:$newLength}
  convert -background none $i $newName.png
done

for i in *png;do 
 convert $i -resize 36x36 ../res/drawable-ldpi/$i; 
 convert $i -resize 48x48 ../res/drawable-mdpi/$i; 
 convert $i -resize 72x72 ../res/drawable-hdpi/$i; 
 convert $i -resize 96x96 ../res/drawable-xhdpi/$i; 
done

rm *png
