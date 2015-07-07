// Custom jQuery
// ----------------------------------- 


(function(window, document, $, undefined){

  $(function(){
  
    var element = $('[data-vector-map]');
    
    new VectorMap(element, seriesData, markersData);
    
  });

  var seriesData = {
    'CA': 11100,   // Canada
    'DE': 2510,    // Germany
    'FR': 3710,    // France
    'AU': 5710,    // Australia
    'GB': 8310,    // Great Britain
    'RU': 9310,    // Russia
    'BR': 6610,    // Brazil
    'IN': 7810,    // India
    'CN': 4310,    // China
    'US': 839,     // USA
    'SA': 410      // Saudi Arabia
  };
  
  var markersData = [
    { latLng:[41.90, 12.45],  name:'Vatican City'          },
    { latLng:[43.73, 7.41],   name:'Monaco'                },
    { latLng:[-0.52, 166.93], name:'Nauru'                 },
    { latLng:[-8.51, 179.21], name:'Tuvalu'                },
    { latLng:[7.11,171.06],   name:'Marshall Islands'      },
    { latLng:[17.3,-62.73],   name:'Saint Kitts and Nevis' },
    { latLng:[3.2,73.22],     name:'Maldives'              },
    { latLng:[35.88,14.5],    name:'Malta'                 },
    { latLng:[41.0,-71.06],   name:'New England'           },
    { latLng:[12.05,-61.75],  name:'Grenada'               },
    { latLng:[13.16,-59.55],  name:'Barbados'              },
    { latLng:[17.11,-61.85],  name:'Antigua and Barbuda'   },
    { latLng:[-4.61,55.45],   name:'Seychelles'            },
    { latLng:[7.35,134.46],   name:'Palau'                 },
    { latLng:[42.5,1.51],     name:'Andorra'               }
  ];


})(window, document, window.jQuery);
