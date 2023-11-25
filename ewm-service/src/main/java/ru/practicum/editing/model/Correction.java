package ru.practicum.editing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.editing.dto.CorrectionAuthor;
import ru.practicum.editing.dto.EventField;
import ru.practicum.editing.dto.RevisionState;
import ru.practicum.event.model.Event;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "corrections")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Correction {

    @Id
    @Column(name = "correction_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "event_field", nullable = false, updatable = false)
    private EventField eventField;

    @Column(name = "admin_note", nullable = false)
    private String adminNote;

    @Column(name = "before_edit", nullable = false)
    private String before;

    @Column(name = "after_edit")
    private String after;

    @Column(name = "author", nullable = false, updatable = false)
    private CorrectionAuthor correctionAuthor;


    @Column(name = "revision_state")
    private RevisionState state;
}