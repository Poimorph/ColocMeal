---
title: ColocMeal - Project report 
author:
  - Suhayb Saleh
  - Bastien Laurent
  - Jonathan Damali
  - Amissamba Johnson
date:
lang: en-EN
geometry: margin=1.5cm
papersize: a4
fontsize: 11pt
documentclass: article
toc: true
toc-title: Table of Contents
toc-depth: 3
numbersections: true
colorlinks: true
linkcolor: blue
highlight-style: tango
header-includes: |
  \usepackage{tcolorbox}
  \tcbuselibrary{breakable}
  \usepackage{etoolbox}
  \usepackage{fvextra}
  \definecolor{codebg}{RGB}{246,248,250}
  \definecolor{codeborder}{RGB}{230,230,230}
  \DefineVerbatimEnvironment{Highlighting}{Verbatim}{breaklines,commandchars=\\\{\}}
  \BeforeBeginEnvironment{Highlighting}{
    \begin{tcolorbox}[
      breakable,
      colback=codebg,
      colframe=codeborder,
      arc=3pt,
      boxrule=0.5pt,
      left=5pt,
      right=5pt,
      top=5pt,
      bottom=5pt
    ]
  }
  \AfterEndEnvironment{Highlighting}{\end{tcolorbox}}
  \usepackage{xcolor}
  \definecolor{quotebar}{RGB}{99,102,241}
  \definecolor{quotebg}{RGB}{238,239,255}
  \renewenvironment{quote}{%
  \begin{tcolorbox}[
  breakable,
  colback=quotebg,
  colframe=quotebar,
  leftrule=4pt,
  rightrule=0pt,
  toprule=0pt,
  bottomrule=0pt,
  arc=0pt,
  left=10pt,
  right=8pt,
  top=6pt,
  bottom=6pt
  ]
  \itshape
  }{%
  \end{tcolorbox}
  }
---

# Project Specification










