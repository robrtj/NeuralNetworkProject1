unset colorbox

set autoscale

set datafile separator ","


set term png
set output "regressionPlot.png"


plot 'tests/data.xsq.train.csv' using 1:2 pt 6 ps 2, 'tests/reg_ans.csv' using 1:2 notitle pt 5 ps 1