unset colorbox

set autoscale

set datafile separator ","


set term png
set output "regressionPlot.png"


plot 'tests/abc.ans.csv' using 1:2 notitle pt 5 ps 1, 'tests/data.xsq.train.csv' using 1:2 notitle with circles