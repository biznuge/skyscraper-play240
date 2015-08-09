# skyscraper-play240

This app is a first stab at looking at play 2.4.x with scala 2.11 and mongodb support provided through reactivemongo + the play-reactivemongo plugin.

The application scrapes the sky sports news RSS feed at http://www.skysports.com/feeds/11095/news.xml and if required scrapes the page (non blocking to the request).

It then provides get / pagination and search methods via angular in the client which hit play contoller methods for the relevant content.

All methods have been provided as GET. This could have been done as POST, but search queries, since they may be done in high volume in response to events, could easily be cached in any proxying envinronment pre application with a GET, so I figured just go with this.



