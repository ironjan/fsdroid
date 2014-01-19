#!/bin/bash
RES=../fsdroid/res

for i in *svg; do
  length=${#i}
  newLength=length-4
  newName=${i:0:$newLength}
  convert -background none $i -resize 512x512 $newName.png
done

for i in *png;do 
  f=$RES/drawable-mdpi/$i
  convert $i -resize 48x48 $f
  
  f=$RES/drawable-hdpi/$i
  convert $i -resize 72x72 $f

  f=$RES/drawable-xhdpi/$i
  convert $i -resize 96x96 $f

  f=$RES/drawable-xxhdpi/$i
  convert $i -resize 144x144 $f
done


echo "Cleaning up"
rm *png

i=ic_launcher.png
convert -background none ic_launcher.svg -resize 512x512 ../store/high-res-app-logo.png
convert ../high-res-app-logo.png -resize 48x48 $RES/drawable-mdpi/$i
convert ../high-res-app-logo.png -resize 72x72 $RES/drawable-hdpi/$i
convert ../high-res-app-logo.png -resize 96x96 $RES/drawable-xhdpi/$i
convert ../high-res-app-logo.png -resize 144x144 $RES/drawable-xxhdpi/$i

