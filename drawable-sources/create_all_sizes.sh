#!/bin/bash
RES=../fsdroid/res

for i in *svg; do
  length=${#i}
  newLength=length-4
  newName=${i:0:$newLength}
  convert -background none $i $newName.png
done

for i in *png;do 
  # MDPI is 32:32, hdpi, xhdpi 2:3:4
  f=$RES/drawable-mdpi/$i
  convert $i -resize 32x32 $f
  
  f=$RES/drawable-hdpi/$i
  convert $i -resize 48x48 $f

  f=$RES/drawable-xhdpi/$i
  convert $i -resize 64x64 $f
done

convert -background none ic_launcher.svg -resize 512x512 ../high-res-app-logo.png

rm *png
