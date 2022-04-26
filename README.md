# Deadlocks on Spring Data example

## The introduction

Detecting potential deadlocks was never easy. Testing against them - even harder. A faulty solution may produce a deadlock as rarely as once per month. Paradoxically, we can cure deadlocks by increasing the likelihood of them happening. Only when we are able to reproduce them reliably, we can verify that we know how to prevent them.

## The project

**A Spring Data JPA project showcasing deadlock scenarios and ways of avoiding them.** (Also, the project demonstrates how to setup multiple databases for a single project: check the **1_multiple_databases** branch)

The tests require **infrastructure/docker-compose.yaml** running (one of the images has to be built from the infrastructure/Dockerfile). Some of the tests are failing, because some of the included solutions are purposefully erroneous.

