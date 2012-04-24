function TimeUnit() {
	
	this.positive = [];
	this.negative = [];
	this.neutral = [];

	this.trust = 0.7;

	this.add = function(tweet) {
		if (tweet.opinion.prob < this.trust) {
			this.neutral.push(tweet);
		} else if (tweet.opinion.mood == 'positive') {
    		this.positive.push(tweet);
    	} else {
    		this.negative.push(tweet);
    	}
	}

	this.max = function() {
		return Math.max(this.positive.length, this.negative.length, this.neutral.length);
	}
}