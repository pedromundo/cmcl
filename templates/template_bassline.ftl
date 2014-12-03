\score {
  \new Staff <<
    \new Voice \relative g, {
      \set midiInstrument = #"fx 1 (rain)"
      \voiceOne
      \key g
      \time 4/4
      <#assign keys = months?keys>
      <#list keys as key>
	\tempo 4 = ${months[key]}
	c c c c	
      </#list>
    }       
  >>  
  \layout { }
  \midi {
    \context {
      \Staff
      \remove "Staff_performer"
    }
    \context {
      \Voice
      \consists "Staff_performer"
    }
    \tempo 4 = 220
  }
}