package com.mapu.domain.figure.application.response;

import com.mapu.domain.figure.domain.Figure;
import com.mapu.domain.figure.domain.FigureTag;
import com.mapu.domain.figure.domain.FigureRate;
import com.mapu.domain.figure.domain.FigureRelation;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FigureResponseDTO {
    private Long figureId;
    private String type;
    private String address;
    private int round;
    private int area;
    private int length;
    private String name;
    private String detailAddress;
    private List<TagDTO> tags;
    private List<RateDTO> rates;
    private List<RelatedFigureDTO> relatedFigures;

    public FigureResponseDTO(Figure figure) {
        this.figureId = figure.getId();
        this.type = String.valueOf(figure.getType());
        this.address = figure.getAddress();
        this.round = figure.getRound();
        this.area = figure.getArea();
        this.length = figure.getLength();
        this.name = figure.getName();
        this.detailAddress = figure.getText();
        this.tags = figure.getTags().stream().map(TagDTO::new).collect(Collectors.toList());
        this.rates = figure.getRate().stream().map(RateDTO::new).collect(Collectors.toList());
        this.relatedFigures = figure.getRelation().stream()
                .map(relation -> new RelatedFigureDTO(relation, figure))
                .collect(Collectors.toList());
    }

    @Getter
    public static class TagDTO {
        private Long tagId;
        private String tag;

        public TagDTO(FigureTag figureTag) {
            this.tagId = figureTag.getId();
            this.tag = figureTag.getTag();
        }
    }

    @Getter
    public static class RateDTO {
        private Long rateId;
        private String rateName;
        private int rateStar;

        public RateDTO(FigureRate figureRate) {
            this.rateId = figureRate.getId();
            this.rateName = figureRate.getRateName();
            this.rateStar = figureRate.getRateStar();
        }
    }

    @Getter
    public static class RelatedFigureDTO {
        private Long relationId;
        private String name;
        private String address;
        private String type;

        public RelatedFigureDTO(FigureRelation relation, Figure currentFigure) {
            Figure relatedFigure = relation.getFigure().equals(currentFigure) ? relation.getFigure2() : relation.getFigure();
            this.relationId = relatedFigure.getId();
            this.name = relatedFigure.getName();
            this.address = relatedFigure.getAddress();
            this.type = String.valueOf(relatedFigure.getType());
        }
    }

    public static FigureResponseDTO from(Figure figure) {
        return new FigureResponseDTO(figure);
    }
}