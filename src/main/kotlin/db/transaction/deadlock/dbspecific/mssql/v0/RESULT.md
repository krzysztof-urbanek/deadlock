<h2>0. The concurrent update problem.<br/><br/></h2>

a) Process (1) <b>reads</b> the record with mentions = 0<br/>
b) Process (2) <b>reads</b> the record with mentions = 0


c) Process (1) <b>changes</b> its cashed record mentions from 0 to 1<br/>
d) Process (2) <b>changes</b> its cashed record mentions from 0 to 1


e) Process (1) <b>updates</b> the database with the cashed record (mentions = 1)<br/>
f) Process (2) <b>updates</b> the database with the cashed record (mentions = 1)
