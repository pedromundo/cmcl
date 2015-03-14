\score {
  \new Staff <<
    \new Voice{
      \set midiInstrument = #"Taiko Drum"
      \voiceOne      
      \time 4/4
      \tempo 4 = 120
      	  d''
	  d''
	  c,
	  c,
	  g,
	  g,
	  d,
	  d,
	  a''
	  a''
	  a''
	  a''
	  a''
	  a''
	  a''
	  a''
	  d,
	  d,
	  d,
	  d,
	  d,
	  d,
	  d,
	  d,
	  e,
	  e,
	  e,
	  e,
	  e,
	  e,
	  e,
	  e,
	  d,
	  d,
	  e''
	  e''
	  e''
	  e''
	  e''
	  e''
	  e''
	  e''
	  c'''''
	  c'''''
	  c'''''
	  c'''''
	  c'''''
	  c'''''
	  c'''''
	  c'''''
	  d,
	  d,
	  d,
	  d,
	  d,
	  d,
	  d,
	  d,
	  c,
	  c,
	  d,
	  d,
	  c,
	  c,
	  c,
	  c,
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