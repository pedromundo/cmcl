\score {
  \new Staff <<
    \new Voice{
      \set midiInstrument = #"Taiko Drum"
      \voiceOne
      \key g
      \time 4/4
      \tempo 4 = 120
      <#assign keys = months?keys>           
      <#list keys as key>
	<#list 1..4 as i>
	  <#assign temp = months[key]>
	  ${noteMap[temp?string]}
	</#list>
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