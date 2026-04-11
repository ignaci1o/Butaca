export const mockEvents = [
  {
    id: 1,
    slug: "my-chemical-romance-barclays-center",
    nombre: "The Black Parade Is Dead - World Tour",
    artista: "My Chemical Romance",
    categoria: "Conciertos",
    ciudad: "Nueva York",
    recinto: "Barclays Center",
    fecha: "2026-03-20",
    hora: "21:00",
    precioMinimo: 59,
    precioMaximo: 280,
    destacados: true,
    popular: true,
    disponibilidad: 214,
    imagen:
      "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?auto=format&fit=crop&w=1200&q=80",
    descripcion:
      "Una fecha de alto impacto con una puesta oscura, teatral y pensada para una audiencia masiva. Ideal para destacar en la home.",
    etiquetas: ["Agotandose", "Gira mundial", "Rock alternativo"],
    tiposEntrada: [
      { id: "general", nombre: "General", precio: 59 },
      { id: "campo", nombre: "Campo preferente", precio: 110 },
      { id: "platea", nombre: "Platea baja", precio: 165 },
      { id: "vip", nombre: "Experiencia VIP", precio: 280 }
    ]
  },
  {
    id: 2,
    slug: "dua-lipa-movistar-arena",
    nombre: "Future Glow Live",
    artista: "Dua Lipa",
    categoria: "Conciertos",
    ciudad: "Buenos Aires",
    recinto: "Movistar Arena",
    fecha: "2026-05-08",
    hora: "20:30",
    precioMinimo: 48,
    precioMaximo: 210,
    destacados: true,
    popular: true,
    disponibilidad: 318,
    imagen:
      "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?auto=format&fit=crop&w=1200&q=80",
    descripcion:
      "Show pop con visuales intensos, escenario envolvente y sectores para distintas experiencias de compra.",
    etiquetas: ["Estreno", "Pop", "Alta demanda"],
    tiposEntrada: [
      { id: "general", nombre: "General", precio: 48 },
      { id: "campo", nombre: "Campo", precio: 92 },
      { id: "platea", nombre: "Platea", precio: 148 },
      { id: "vip", nombre: "VIP lounge", precio: 210 }
    ]
  },
  {
    id: 3,
    slug: "clasico-capital-estadio-sur",
    nombre: "Clasico de la Capital",
    artista: "Rojo FC vs Azul Club",
    categoria: "Deportes",
    ciudad: "Montevideo",
    recinto: "Estadio Sur",
    fecha: "2026-04-28",
    hora: "18:00",
    precioMinimo: 22,
    precioMaximo: 95,
    destacados: true,
    popular: false,
    disponibilidad: 604,
    imagen:
      "https://images.unsplash.com/photo-1547347298-4074fc3086f0?auto=format&fit=crop&w=1200&q=80",
    descripcion:
      "Partido de alta convocatoria con sectores familiares, plateas laterales y experiencia premium en palco.",
    etiquetas: ["Futbol", "Fin de semana", "Popular"],
    tiposEntrada: [
      { id: "cabecera", nombre: "Cabecera", precio: 22 },
      { id: "platea", nombre: "Platea lateral", precio: 44 },
      { id: "baja", nombre: "Platea baja", precio: 65 },
      { id: "palco", nombre: "Palco premium", precio: 95 }
    ]
  },
  {
    id: 4,
    slug: "hamlet-teatro-real",
    nombre: "Hamlet",
    artista: "Compania Nacional",
    categoria: "Teatro",
    ciudad: "Madrid",
    recinto: "Teatro Real",
    fecha: "2026-06-11",
    hora: "19:30",
    precioMinimo: 30,
    precioMaximo: 130,
    destacados: false,
    popular: true,
    disponibilidad: 92,
    imagen:
      "https://images.unsplash.com/photo-1503095396549-807759245b35?auto=format&fit=crop&w=1200&q=80",
    descripcion:
      "Montaje contemporaneo con una puesta sobria y un sistema de ticketing centrado en sectores numerados.",
    etiquetas: ["Ultimas filas", "Drama", "Sala historica"],
    tiposEntrada: [
      { id: "general", nombre: "General", precio: 30 },
      { id: "preferente", nombre: "Preferente", precio: 58 },
      { id: "platea", nombre: "Platea central", precio: 82 },
      { id: "premium", nombre: "Fila premium", precio: 130 }
    ]
  },
  {
    id: 5,
    slug: "festival-luces-rio",
    nombre: "Festival Luces del Rio",
    artista: "Varios artistas",
    categoria: "Festivales",
    ciudad: "Rosario",
    recinto: "Parque del Rio",
    fecha: "2026-02-15",
    hora: "16:00",
    precioMinimo: 18,
    precioMaximo: 140,
    destacados: false,
    popular: true,
    disponibilidad: 740,
    imagen:
      "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=1200&q=80",
    descripcion:
      "Festival al aire libre con escenario principal, zona gastronomica y una grilla amplia para publico joven.",
    etiquetas: ["Outdoor", "Multi escenario", "Verano"],
    tiposEntrada: [
      { id: "general", nombre: "General", precio: 18 },
      { id: "campo", nombre: "Campo cercano", precio: 52 },
      { id: "lounge", nombre: "Lounge", precio: 96 },
      { id: "vip", nombre: "VIP backstage", precio: 140 }
    ]
  },
  {
    id: 6,
    slug: "expo-inmersiva-museo-central",
    nombre: "Expo Inmersiva Orbita",
    artista: "Colectivo Orbita",
    categoria: "Arte",
    ciudad: "Ciudad de Mexico",
    recinto: "Museo Central",
    fecha: "2026-07-04",
    hora: "17:30",
    precioMinimo: 12,
    precioMaximo: 48,
    destacados: false,
    popular: false,
    disponibilidad: 126,
    imagen:
      "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=80",
    descripcion:
      "Experiencia visual y sonora con entradas por franja horaria, ideal para probar integracion futura con slots.",
    etiquetas: ["Inmersivo", "Museo", "Visuales"],
    tiposEntrada: [
      { id: "general", nombre: "Ingreso general", precio: 12 },
      { id: "flex", nombre: "Ingreso flexible", precio: 25 },
      { id: "guiado", nombre: "Recorrido guiado", precio: 36 },
      { id: "premium", nombre: "Acceso premium", precio: 48 }
    ]
  }
];

export const categoryOptions = [
  "Todos",
  "Conciertos",
  "Deportes",
  "Teatro",
  "Festivales",
  "Arte"
];
