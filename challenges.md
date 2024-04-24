# Technical challenges

## Running UI tests in eclipse che

**challenge discription**
We had issues enabling UI tests in eclipse che. Che will not automatically open port 6080 and run the UI test in the background. We tried editing the devfile.yaml multiple times, however, we had no success.
The cause for this issue is probably that che is not completely optimal with our devfile. Our hypothesis is that che is stuck with the first image it was provided, and that we therefore are not able to provide it with the image we need to run the UI tests in che.
However, through the process, we leared about TCP connections and that different processes can run on different ports, which will come in handy.
