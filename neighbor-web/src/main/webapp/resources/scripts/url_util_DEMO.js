var isInIFrame = (window.location != window.parent.location), name_url, full_url;
if (isInIFrame == true) {
	// do nothing if is in iframe
} else {
	// redirect if is not in iframe
	name_url = 'neighbor-demo';
	full_url = location.href;
	// alert(full_url);
	// location.href =
	// full_url.substr(0,(location.href.indexOf(name_url)+(name_url).length));
}