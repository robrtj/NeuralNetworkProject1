unset colorbox

set autoscale

set datafile separator ","


set term png
set output "classificationPlot.png"

set palette rgb 33,13,10
plot 'tests/cl_ans.csv' using 1:2:3 notitle pt 5 ps 1 palette, 'tests/data.train.csv' using 1:2:($3 + 3) notitle pt 6 ps 2 palette