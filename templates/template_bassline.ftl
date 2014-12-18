\score {
  \new Staff <<
    \new Voice{
      \set midiInstrument = #"Taiko Drum"
      \voiceOne      
      \time 4/4
      \tempo 4 = 120
      <#assign keys = commitsMonth?keys>           
      <#list keys as key>
	<#assign numNotas = committersMonth[key]>
	<#list 1..numNotas as i>
	  <#assign temp = commitsMonth[key]>
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
  }
}