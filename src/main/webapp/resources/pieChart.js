function chartExtender() {
    var options = {
        plugins: [ChartDataLabels],
        options: {
            plugins: {
                // Change options for ALL labels of THIS CHART
                datalabels: {
                    color: 'HotPink'
                }
            }
        },
        data: {
            datasets: [{
                // Change options only for labels of THIS DATASET
                datalabels: {
                    color: 'Indigo'
                }
            }]
        }
    };

    //merge all options into the main chart options
    $.extend(true, this.cfg.config, options);
}