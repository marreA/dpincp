dimensions <- c(5,10,25,35,50,75,100,125,150,175,200,250,300,350)

#dimensions <- c(5,10,15,20,25,35,50,75,100,125,150,175,200,225,250,300,350)

dim = 4
mydata = read.csv(paste("~/dpincp/results/weakly_correlated/results_binary_weaklycorrelated_", dimensions[1], "_100_50.csv", sep = ""))
names = colnames(mydata)
names = names[-length(names)]
dnames = lapply(dimensions,toString.default)

results = matrix(-1, ncol = length(names), nrow = length(dimensions))
colnames(results) = names
rownames(results) = dnames

ndim = length(dimensions)

for (dim in dimensions) {
  mydata = read.csv(paste("~/dpincp/results/weakly_correlated/results_binary_weaklycorrelated_", dim, "_100_50.csv", sep = ""))
  mydata = apply(mydata,2,abs)
  for (i in 1:(ncol(mydata)-1)) {
    n = colnames(mydata)[i]
    val = mean(mydata[,i])
    results[toString(dim),n] = val
  }
  
}


results = results[,-c(2,3,5,6,7)]
#colnames(results) <- c("Normal DP", "Naive in CP", "Global constraint", "DPE", "DPE + sr", "DPE + sr + sorting", "MIP flow formulation")
#names <-  c("Normal DP", "Naive in CP", "Global constraint", "DPE", "DPE + sr", "DPE + sr + sorting", "MIP flow formulation")
# Create Line Chart
#results = results[,-3]
#colnames(results) <- c("Normal DP", "Naive in CP", "Global constraint", "DPE", "MIP flow formulation")
#names <- c("Normal DP", "Naive in CP", "Global constraint", "DPE", "MIP flow formulation")
#pdf("~/plot.pdf")

# get the range for the x and y axis 
xrange <- range(dimensions)
yrange <- range(0,results) 

# set up the plot 
plot(xrange,yrange, type="n", xlab="Number of items", ylab="Average computational time" ) 
colors <- rainbow(length(names))
colors[2] <- "#000000FF"
linetype <- c(1:length(names)) 
plotchar <- seq(18,18+length(names),1)

# add lines 
for (i in 1:length(names)) { 
  
  tree <- results[which(results[,i]>0),i]
  lines(dimensions[which(results[,i]>0)], tree, type="b", lwd=1.5, lty=linetype[i], col=colors[i], pch=plotchar[i]) 
} 

# add a title and subtitle 
#title(ds)

# add a legend 
legend(xrange[2]*0.4, yrange[1]+ 1*(yrange[2]-yrange[1]), names, cex=0.8, col=colors,pch=plotchar, lty=linetype)

