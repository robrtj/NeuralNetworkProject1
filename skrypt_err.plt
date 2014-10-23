unset colorbox

set autoscale

set datafile separator ","


set term png
set output "errorPlot.png"


plot 'tests/err_train_ans.csv' using 1:2 with line, 'tests/err_test_ans.csv' using 1:2 with line