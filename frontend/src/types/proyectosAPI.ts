export interface Proyectos {
  proyectos: Proyecto[];
  costoTotalGlobal: number;
}

export interface Proyecto {
  nombreProyecto: string;
  costoPorMes: { [key: string]: number };
  costoTotal: number;
}
