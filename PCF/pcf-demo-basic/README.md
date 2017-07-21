cf login -a https://api.run.pivotal.io

cf push

cf ssh  dmw-pcf-demo-basic

cf stop dmw-pcf-demo-basic 

cf ssh -L 10009:localhost:10009 dmw-pcf-demo-basic