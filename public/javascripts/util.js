function twitterStringToDate (s) {
    var b = s.split(/[: ]/g);
    var m = {jan:0, feb:1, mar:2, apr:3, may:4, jun:5, jul:6, aug:7, sep:8, oct:9, nov:10, dec:11};
    return new Date(Date.UTC(b[7], m[b[1].toLowerCase()], b[2], b[3], b[4], b[5]));
}
