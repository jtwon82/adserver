mongo 127.0.01:10001/billing --eval "load('/home/dreamsearch/scripts/billing.createCollection.js')" >> /home/dreamsearch/logs/cron/billing.createCollection.log.`date +%y%m%d` 2>&1; echo '1/1E.ok'