dimensions <- c(5,10,15,20,25)
dim = 30
mydata = read.csv(paste("~/intelliJ/knapsackCP/results_binary_correlated_", dimensions[1], "_20_5000.csv", sep = ""))
results = mydata[1,]
names = colnames(mydata)

names = names[-length(names)]
dnames = lapply(dimensions,toString.default)

results = matrix(-1, ncol = length(names), nrow = length(dimensions))
colnames(results) = names
rownames(results) = dnames

ndim = length(dimensions)

for (dim in dimensions) {
  mydata = read.csv(paste("~/intelliJ/knapsackCP/results_binary_correlated_", dim, "_20_5000.csv", sep = ""))
  for (i in 1:(ncol(mydata)-1)) {
    n = colnames(mydata)[i]
    val = mean(mydata[,i])
    results[toString(dim),n] = val
  }
  
}
  
results


# Create Line Chart

pdf("~/plot.pdf")

# get the range for the x and y axis 
xrange <- range(dimensions)
yrange <- range(0,results) 

# set up the plot 
plot(xrange,yrange, type="n", xlab="Number of extracted features", ylab="Correct Classification Rate (%)" ) 
colors <- rainbow(length(names)) 
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
legend(xrange[2]*0.4, yrange[1]+ 0.7*(yrange[2]-yrange[1]), names, cex=0.8, col=colors,pch=plotchar, lty=linetype)


#dev.off()
if (filename != FALSE)
{
  write.table(result_matrix, file = paste("~/results/",filename,".txt"), col.names = c("seed","l2pca","PCAproj","PCAgrid","pcal1","l1pca","l1pcastar","l1pcahp","Time l2pca","Time PCAproj","Time PCAgrid","Time pcal1","Time l1pca","Time l1pcastar","Time l1pcahp", "Id dataset", "ProjDim"),sep = ";")
} else {
  write.table(result_matrix, file = paste("~/results/res_",ds,"_",shuffN,".txt"), col.names = c("seed","l2pca","PCAproj","PCAgrid","pcal1","l1pca","l1pcastar","l1pcahp","Time l2pca","Time PCAproj","Time PCAgrid","Time pcal1","Time l1pca","Time l1pcastar","Time l1pcahp", "Id dataset", "ProjDim"),sep = ";")
}



